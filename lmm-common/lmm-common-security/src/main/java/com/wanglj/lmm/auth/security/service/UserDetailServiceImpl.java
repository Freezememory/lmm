//package com.wanglj.lmm.auth.security.service;
//
//import com.wanglj.lmm.auth.security.entity.User;
//import com.wanglj.lmm.protocol.feign.RemoteUserService;
//import com.wanglj.lmm.protocol.feign.SysUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * @Author: Wanglj
// * @Date: 2023/1/10 14:42
// * @Description :
// */
//@RequiredArgsConstructor
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    private final RemoteUserService remoteUserService;
//
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        SysUser info = remoteUserService.info();
//
//
//        return new User(info);
//    }
//}
