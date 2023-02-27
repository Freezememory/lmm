package com.wanglj.lmm.admin.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.service.SysOrganizationService;
import com.wanglj.lmm.common.base.constant.BfjConstants;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.admin.api.entity.SysOrganization;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.api.req.SysOrgReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统机构信息管理
 */
@Slf4j
@RestController
@RequestMapping(BfjConstants.ADMIN_URL_PREFIX + "/org")
@RequiredArgsConstructor
public class SysOrganizationController {

    private final SysOrganizationService sysOrganizationService;

    /**
     * 机构分页查询
     * @param req 查询条件
     * @return: Page<SysUserVO>
     */
    @PostMapping("/page")
    public R<Page<SysOrganization>> page(@RequestBody SysOrgReq req) {
        return R.ok(AdminCode.Success, sysOrganizationService.page(req));

    }

    /**
     * 新增机构信息
     * @param req 保存机构信息
     * @return: Boolean
     */
    @PostMapping("/save")
    public R save(@RequestBody SysOrganization req) {
        if (ObjectUtil.isNull(req.getOrgCode()) || ObjectUtil.isNull(req.getOrgName()) || ObjectUtil.isNull(req.getOrgType())
                || ObjectUtil.isNull(req.getOrgLevel()) || ObjectUtil.isNull(req.getProvince()) || ObjectUtil.isNull(req.getCity())
                || ObjectUtil.isNull(req.getCounty()) || ObjectUtil.isNull(req.getMutualRecStatus())) {
            return R.fail(AdminCode.Fail, "必填项不可为空");
        }
        return R.ok(AdminCode.Success, sysOrganizationService.saveInfo(req));
    }

    /**
     * 编辑机构信息
     * @param req 更新机构信息
     * @return: Boolean
     */
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody SysOrganization req) {
        return R.ok(AdminCode.Success, sysOrganizationService.updateInfo(req));
    }

    /**
     * 删除机构
     * @param req 机构id
     * @return: Boolean
     */
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody SysOrgReq req) {
        return R.ok(AdminCode.Success, sysOrganizationService.delete(req));
    }

    /**
     * 导出机构信息
     * @param req 导出条件
     */
    @PostMapping("/exportInfo")
    public void exportInfo(@RequestBody SysOrgReq req) {
        sysOrganizationService.exportInfo(req);
    }

    /**
     * 上传文件保存机构信息
     * @param file 机构信息excel
     * @return: Boolean
     */
    @PostMapping("/uploadExcel")
    public R<Boolean> uploadExcel(MultipartFile file) {
        return R.ok(AdminCode.Success, sysOrganizationService.uploadExcel(file));
    }

}
