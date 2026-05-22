package com.diary.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.user.dto.LoginDTO;
import com.diary.user.dto.LoginVO;
import com.diary.user.dto.RegisterDTO;
import com.diary.user.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    void register(RegisterDTO dto);

    LoginVO login(LoginDTO dto);

    SysUser getUserById(Long id);
}
