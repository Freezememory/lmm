package com.diary.user.controller;

import com.diary.common.core.domain.R;
import com.diary.user.dto.LoginDTO;
import com.diary.user.dto.LoginVO;
import com.diary.user.dto.RegisterDTO;
import com.diary.user.entity.SysUser;
import com.diary.user.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        sysUserService.register(dto);
        return R.ok();
    }

    @PostMapping("/login/account")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(sysUserService.login(dto));
    }

    @GetMapping("/info")
    public R<SysUser> getUserInfo(@RequestHeader("X-User-Id") Long userId) {
        return R.ok(sysUserService.getUserById(userId));
    }
}
