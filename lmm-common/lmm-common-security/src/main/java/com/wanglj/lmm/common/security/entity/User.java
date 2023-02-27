//package com.wanglj.lmm.auth.security.entity;
//
//import com.wanglj.lmm.protocol.feign.SysUser;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
///**
// * @Author: Wanglj
// * @Date: 2023/1/10 22:41
// * @Description :
// */
//@AllArgsConstructor
//@Data
//@NoArgsConstructor
//public class User implements UserDetails {
//
//    private SysUser sysUser;
//
///*    public User(SysUser sysUser){
//        this.sysUser = sysUser;
//    }*/
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return sysUser.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return sysUser.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
