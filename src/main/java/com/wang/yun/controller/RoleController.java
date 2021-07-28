package com.wang.yun.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.Constant;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.ResultObject;
import com.wang.yun.common.TreeNode;
import com.wang.yun.pojo.Permission;
import com.wang.yun.pojo.Role;
import com.wang.yun.service.PermissionService;
import com.wang.yun.service.RoleService;
import com.wang.yun.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2019-09-24
 */
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;
	
	
	/**
	 * 查询
	 */
	@RequestMapping("loadAllRole")
	public DataGridView loadAllRole(RoleVo roleVo) {
		IPage<Role> page=new Page<>(roleVo.getPage(), roleVo.getLimit());
		QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()), "name", roleVo.getName());
		queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()), "remark", roleVo.getRemark());
		queryWrapper.eq(roleVo.getAvailable()!=null, "available", roleVo.getAvailable());
		queryWrapper.orderByDesc("createtime");
		this.roleService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}
	
	
	/**
	 * 添加
	 */
	@RequestMapping("addRole")
	public ResultObject addRole(RoleVo roleVo) {
		try {
			roleVo.setCreatetime(new Date());
			this.roleService.save(roleVo);
			return ResultObject.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.ADD_ERROR;
		}
	}
	/**
	 * 修改
	 */
	@RequestMapping("updateRole")
	public ResultObject updateRole(RoleVo roleVo) {
		try {
			this.roleService.updateById(roleVo);
			return ResultObject.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.UPDATE_ERROR;
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("deleteRole")
	public ResultObject deleteRole(Integer id) {
		try {
			this.roleService.removeById(id);
			return ResultObject.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.DELETE_ERROR;
		}
	}
	
	
	/**
	 * 根据角色ID加载菜单和权限的树的json串
	 */
	@RequestMapping("initPermissionByRoleId")
	public DataGridView initPermissionByRoleId(Integer roleId) {
		//查询所有可用的菜单和权限
		QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
		List<Permission> allPermissions = permissionService.list(queryWrapper);
		/**
		 * 1,根据角色ID查询当前角色拥有的所有的权限或菜单ID
		 * 2,根据查询出来的菜单ID查询权限和菜单数据
		 */
		List<Integer> currentRolePermissions=this.roleService.queryRolePermissionIdsByRid(roleId);
		List<Permission> carrentPermissions=null;
		if(currentRolePermissions.size()>0) { //如果有ID就去查询
			queryWrapper.in("id", currentRolePermissions);		
			carrentPermissions = permissionService.list(queryWrapper);
		}else {
			carrentPermissions=new ArrayList<>();
		}
		//构造 List<TreeNode>
		List<TreeNode> nodes=new ArrayList<>();
		for (Permission p1 : allPermissions) {
			String checkArr="0";
			for (Permission p2 : carrentPermissions) {
				if(p1.getId()==p2.getId()) {
					checkArr="1";
					break;
				}
			}
			Boolean spread=(p1.getOpen()==null||p1.getOpen()==1)?true:false;
			nodes.add(new TreeNode(p1.getId(), p1.getPid(), p1.getTitle(), spread, checkArr));
		}
		return new DataGridView(nodes);
	}
	
	/**
	 * 保存角色和菜单权限之间的关系
	 */
	@RequestMapping("saveRolePermission")
	public ResultObject saveRolePermission(Integer rid,Integer[] ids) {
		try {
			this.roleService.saveRolePermission(rid,ids);
			return ResultObject.DISPATCH_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.DISPATCH_ERROR;
		}
	}
}
