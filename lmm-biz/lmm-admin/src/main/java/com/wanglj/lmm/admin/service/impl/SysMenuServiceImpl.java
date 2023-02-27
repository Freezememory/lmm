package com.wanglj.lmm.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.api.entity.SysMenuModel;
import com.wanglj.lmm.admin.mapper.SysMenuMapper;
import com.wanglj.lmm.admin.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuModel> implements SysMenuService {
}
