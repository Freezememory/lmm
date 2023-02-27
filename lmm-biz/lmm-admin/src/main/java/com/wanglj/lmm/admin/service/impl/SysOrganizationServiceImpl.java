package com.wanglj.lmm.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.excel.SysOrgDataListener;
import com.wanglj.lmm.admin.mapper.SysOrganizationMapper;
import com.wanglj.lmm.admin.service.SysOrganizationService;
import com.wanglj.lmm.admin.api.entity.SysOrganization;
import com.wanglj.lmm.admin.api.req.SysOrgReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements SysOrganizationService {


    @Override
    public Page<SysOrganization> page(SysOrgReq req) {
        LambdaQueryWrapper<SysOrganization> pageWrapper = getWrapper(req);
        Page<SysOrganization> page = baseMapper.selectPage(req.page(), pageWrapper);

        //所有父级机构编码
        List<SysOrganization> list = super.lambdaQuery().select(SysOrganization::getParentOrgId).list();
        List<String> idList = list.stream().map(SysOrganization::getParentOrgId).collect(Collectors.toList());
        //是否存在子级
        for (SysOrganization organization : page.getRecords()) {
            if(idList.contains(organization.getOrgCode())){
                organization.setHasChildren(1);
            }
        }

        return page;
    }


    public List<SysOrganization> list(SysOrgReq req) {
        LambdaQueryWrapper<SysOrganization> listWrapper = getWrapper(req);
        return baseMapper.selectList(listWrapper);
    }

    /**
     * @desc: 拼接查询条件
     * @author: Wanglj
     * @date: 2023/1/13 16:11:06
     * @param req 查询条件信息
     * @return: LambdaQueryWrapper<SysOrganization>
     */
    public LambdaQueryWrapper<SysOrganization> getWrapper(SysOrgReq req) {
        return new LambdaQueryWrapper<SysOrganization>()
                .eq(StrUtil.isNotBlank(req.getOrgCode()), SysOrganization::getOrgCode, req.getOrgCode())
                .eq(StrUtil.isNotBlank(req.getOrgName()), SysOrganization::getOrgName, req.getOrgName())
                .eq(StrUtil.isNotBlank(req.getOrgType()), SysOrganization::getOrgType, req.getOrgType())
                .eq(StrUtil.isNotBlank(req.getOrgLevel()), SysOrganization::getOrgLevel, req.getOrgLevel())
                .eq(StrUtil.isNotBlank(req.getProvince()), SysOrganization::getProvince, req.getProvince())
                .eq(StrUtil.isNotBlank(req.getCity()), SysOrganization::getCity, req.getCity())
                .eq(StrUtil.isNotBlank(req.getCounty()), SysOrganization::getCounty, req.getCounty())
                .eq(ObjectUtil.isNotNull(req.getMutualRecStatus()), SysOrganization::getMutualRecStatus, req.getMutualRecStatus());
    }

    @Override
    public Boolean saveInfo(SysOrganization organization) {
        this.checkInfo(organization);
        return super.save(organization);
    }

    @Override
    public Boolean updateInfo(SysOrganization req) {
        this.checkInfo(req);
        return super.updateById(req);
    }

    /**
     * @desc: 机构信息唯一性检查
     * @author: Wanglj
     * @date: 2023/1/13 16:11:39
     * @param organization 待新增、修改机构信息
     */
    private void checkInfo(SysOrganization organization) {
        //机构编码 、机构名称不可重复
        List<SysOrganization> list = super.lambdaQuery().and(wq -> wq.eq(SysOrganization::getOrgCode, organization.getOrgCode())
                .or()
                .eq(SysOrganization::getOrgName, organization.getOrgName())
        ).list();
        if (CollectionUtil.isNotEmpty(list)) {
            throw new RuntimeException("机构编码 、机构名称不可重复");
        }
    }

    @Override
    public void exportInfo(SysOrgReq req) {
        //文件名需要这样写，不能在setHeader直接写中文名，否则下载的文件名字为空，只有后缀
        String name = "org-" + System.currentTimeMillis() + ".xlsx";
        String fileName = new String(name.getBytes(), StandardCharsets.ISO_8859_1);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/msexcel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        try {
            //文件流写出去
            EasyExcel.write(response.getOutputStream(), SysOrganization.class)
                    .sheet("sheet")
                    .doWrite(this.list(req));

        } catch (IOException e) {
            log.error("导出机构excel异常：{}", req.toString(), e);
        }

        //后台生成文件
        /*EasyExcel.write(name, SysOrganization.class)
                .sheet("sheet")
                .doWrite(list(req));*/
    }

    @Override
    public Boolean uploadExcel(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), SysOrganization.class, new SysOrgDataListener(this)).sheet().doRead();
        } catch (IOException e) {
            log.error("机构信息上传异常",e);
            throw new RuntimeException("机构信息上传异常");
        }
        return true;
    }

    @Override
    public Boolean delete(SysOrgReq req) {
        return super.removeById(req.getOrgId());
    }
}