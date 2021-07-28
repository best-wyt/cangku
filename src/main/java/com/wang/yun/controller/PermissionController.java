package com.wang.yun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.*;
import com.wang.yun.pojo.Permission;
import com.wang.yun.pojo.User;
import com.wang.yun.service.PermissionService;
import com.wang.yun.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/loadIndexLeftPermissionJson")
    public DataGridView loadIndexLeftPermissionJson(PermissionVo permissionVo){
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("type", Constant.TYPE_MENU);
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        List<Permission> list = null;
        User user= (User) WebUtils.getSession().getAttribute("user");
        if (user.getType()==Constant.USER_TYPE_SUPER){
            list=permissionService.list(queryWrapper);
        }else {
            list=permissionService.list(queryWrapper);
        }
        List<TreeNode> treeNodes=new ArrayList<>();
        for (Permission p : list) {
            Integer id=p.getId();
            Integer pid=p.getPid();
            String title=p.getTitle();
            String icon=p.getIcon();
            String href=p.getHref();
            Boolean spread=p.getOpen()==Constant.OPEN_TRUE?true:false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
        }

        List<TreeNode> list2= TreeNodeBuilder.build(treeNodes,1);
//        list2.forEach(System.out::println);
        return new DataGridView(list2);

    }




    @RequestMapping("getMaxOrderNum")
    public Integer getMaxOrderNum(){
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("ordernum");
        List<Permission> list = permissionService.list(wrapper);
        if (list.size()>0){
            return list.get(0).getOrdernum()+1;
        }else {
            return 1;
        }


    }

    @RequestMapping("loadPermissionManagerLeftTreeJson")
    public DataGridView loadPermissionManagerLeftTreeJson(PermissionVo permissionVo){
        QueryWrapper<Permission> wrapper=new QueryWrapper<>();
        wrapper.eq("type",Constant.TYPE_MENU);
        List<Permission> list = permissionService.list(wrapper);
        List<TreeNode> nodes = new ArrayList<>();
        for (Permission permission : list) {
            Boolean spread=permission.getOpen()==1?true:false;
            nodes.add(new TreeNode(permission.getId(),permission.getPid(),permission.getTitle(),spread));
        }
        return new DataGridView(nodes);
    }


    @RequestMapping("/loadAllPermission")
    public DataGridView loadAllPermission(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constant.TYPE_PERMISSION);// 只能查询权限
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()), "percode", permissionVo.getPercode());
        queryWrapper.eq(permissionVo.getId() != null,
                "pid", permissionVo.getId());
        queryWrapper.orderByAsc("ordernum");
        this.permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }


    @RequestMapping("addPermission")
    public ResultObject addPermission(PermissionVo permissionVo){
        try {
            permissionVo.setType(Constant.TYPE_MENU);
            permissionService.save(permissionVo);
            return ResultObject.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.ADD_SUCCESS;
        }

    }

    @RequestMapping("updatePermission")
    public ResultObject updatePermission(PermissionVo permissionVo){
        try {
            permissionService.updateById(permissionVo);
            return ResultObject.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.UPDATE_ERROR;
        }

    }

    @RequestMapping("deletePermission")
    public ResultObject deletePermission(PermissionVo permissionVo){
        try {
            permissionService.removeById(permissionVo.getId());
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }

    }
}
