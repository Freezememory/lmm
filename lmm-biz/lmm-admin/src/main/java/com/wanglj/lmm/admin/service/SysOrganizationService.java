package com.wanglj.lmm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanglj.lmm.admin.api.entity.SysOrganization;
import com.wanglj.lmm.admin.api.req.SysOrgReq;
import org.springframework.web.multipart.MultipartFile;

public interface SysOrganizationService extends IService<SysOrganization> {

    /**
     * @desc: 机构分页查询
     * @author: Wanglj
     * @date: 2023/1/12 14:38:46
     * @param req 查询条件
     * @return: Page<SysUserVO>
     */
    Page<SysOrganization> page(SysOrgReq req);


    /**
     * @desc: 保存机构信息
     * @author: Wanglj
     * @date: 2023/1/12 14:38:49
     * @param req 保存机构信息
     * @return: Boolean
     */
    Boolean saveInfo(SysOrganization req);

    /**
     * @desc: 更新机构信息
     * @author: Wanglj
     * @date: 2023/1/12 14:38:53
     * @param req 更新机构信息
     * @return: Boolean
     */
    Boolean updateInfo(SysOrganization req);

    /**
     * @desc: 删除机构
     * @author: Wanglj
     * @date: 2023/1/12 14:39:39
     * @param req  机构id
     * @return: Boolean
     */
    Boolean delete(SysOrgReq req);

    /**
     * @desc: 导出机构信息
     * @author: Wanglj
     * @date: 2023/1/12 14:38:57
     * @param req 导出条件
     * @return: Boolean
     */
    void exportInfo(SysOrgReq req);

    /**
     * @desc: 上传文件保存机构信息
     * @author: Wanglj
     * @date: 2023/1/12 14:38:59
     * @param   file 机构信息excel
     * @return: Boolean
     */
    Boolean uploadExcel(MultipartFile file);

}
