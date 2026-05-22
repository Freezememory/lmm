package com.diary.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.user.dto.LoginDTO;
import com.diary.user.dto.LoginVO;
import com.diary.user.dto.RegisterDTO;
import com.diary.user.entity.SysUser;
import com.diary.user.mapper.SysUserMapper;
import com.diary.user.service.SysUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private SecretKey secretKey;

    @Value("${jwt.secret:diary-system-secret-key-must-be-at-least-256-bits-long!!}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void register(RegisterDTO dto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, dto.getUsername())
                .or()
                .eq(SysUser::getPhone, dto.getPhone());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("用户名或手机号已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setNickname(dto.getUsername());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        this.save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, dto.getUsername());
        SysUser user = this.getOne(wrapper);

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        String token = generateToken(user.getId());
        return LoginVO.builder()
                .token(token)
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public SysUser getUserById(Long id) {
        return this.getById(id);
    }

    private String generateToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }
}
