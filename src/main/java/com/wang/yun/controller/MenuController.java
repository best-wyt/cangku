package com.wang.yun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.*;
import com.wang.yun.pojo.Permission;
import com.wang.yun.pojo.User;
import com.wang.yun.service.DeptService;
import com.wang.yun.service.PermissionService;
import com.wang.yun.service.RoleService;
import com.wang.yun.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permissionVo){
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("type", Constant.TYPE_MENU);
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        List<Permission> list = null;
        User user= (User) WebUtils.getSession().getAttribute("user");
        if (user.getType()==Constant.USER_TYPE_SUPER){
            list=permissionService.list(queryWrapper);
        }else {
            //根据用户ID+角色+权限去查询
            Integer userId=user.getId();
            //根据用户ID查询角色
            List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(userId);
            //根据角色ID取到权限和菜单ID
            Set<Integer> pids=new HashSet<>();
            for (Integer rid : currentUserRoleIds) {
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissionIds);
            }

            //根据角色ID查询权限
            if(pids.size()>0) {
                queryWrapper.in("id", pids);
                list=permissionService.list(queryWrapper);
            }else {
                list =new ArrayList<>();
            }
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


    @RequestMapping("checkMenuHasChildrenNode")
    public Map<String,Object> checkPermissionHasChildrenNode(PermissionVo permissionVo){
        Map<String,Object> map=new HashMap<>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",permissionVo.getId());
        List<Permission> list = permissionService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
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

    @PostMapping("loadMenuManagerLeftTreeJson")
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


    @RequestMapping("/loadAllMenu")
    public DataGridView loadAllPermission(PermissionVo permissionVo){
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title",permissionVo.getTitle());
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid", permissionVo.getId());
        queryWrapper.eq("type",Constant.TYPE_MENU);
        queryWrapper.orderByAsc("ordernum");
        IPage<Permission> iPage=permissionService.page(new Page<>(permissionVo.getPage(),permissionVo.getLimit()),queryWrapper);
        return new DataGridView(iPage.getTotal(),iPage.getRecords());
    }


    @RequestMapping("addMenu")
    public ResultObject addMenu(PermissionVo permissionVo){
        try {
            permissionVo.setType(Constant.TYPE_MENU);
            permissionService.save(permissionVo);
            return ResultObject.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.ADD_SUCCESS;
        }

    }

    @RequestMapping("updateMenu")
    public ResultObject updateMenu(PermissionVo permissionVo){
        try {
            permissionService.updateById(permissionVo);
            return ResultObject.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.UPDATE_ERROR;
        }

    }

    @RequestMapping("deleteMenu")
    public ResultObject deleteMenu(PermissionVo permissionVo){
        try {
            permissionService.removeById(permissionVo.getId());
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }

    }

}
