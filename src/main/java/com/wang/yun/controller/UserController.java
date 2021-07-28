package com.wang.yun.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.Constant;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.PinyinUtils;
import com.wang.yun.common.ResultObject;
import com.wang.yun.pojo.Dept;
import com.wang.yun.pojo.Role;
import com.wang.yun.pojo.User;
import com.wang.yun.service.DeptService;
import com.wang.yun.service.RoleService;
import com.wang.yun.service.UserService;
import com.wang.yun.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2019-09-20
 */
@RestController
@RequestMapping("/yun/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    /**
     * 用户全查询
     */
    @RequestMapping("loadAllUser")
    public DataGridView loadAllUser(UserVo userVo) {
        IPage<User> page=new Page<>(userVo.getPage(), userVo.getLimit());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getName()), "loginname", userVo.getName())
                .or().eq(StringUtils.isNotBlank(userVo.getName()), "name", userVo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()), "address", userVo.getAddress());
        queryWrapper.eq("type", Constant.USER_TYPE_NORMAL);//查询系统用户
        queryWrapper.eq(userVo.getDeptid()!=null, "deptid",userVo.getDeptid());
        this.userService.page(page, queryWrapper);


        System.out.println(userService.getClass().getSimpleName());
        List<User> list = page.getRecords();
        for (User user : list) {
            Integer deptid = user.getDeptid();
            if(deptid!=null) {
                Dept one =deptService.getById(deptid);
                user.setDeptname(one.getTitle());
            }
            Integer mgr = user.getMgr();
            if(mgr!=null) {
                User one = userService.getById(mgr);
                user.setLeadername(one.getName());
            }
        }

        return new DataGridView(page.getTotal(), list);

    }



    /**
     * 加载最大的排序码
     * @param
     * @return
     */
    @RequestMapping("loadUserMaxOrderNum")
    public Map<String,Object> loadUserMaxOrderNum(){
        Map<String, Object> map=new HashMap<String, Object>();

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<User> page=new Page<>(1, 1);
        List<User> list = this.userService.page(page, queryWrapper).getRecords();
        if(list.size()>0) {
            map.put("value", list.get(0).getOrdernum()+1);
        }else {
            map.put("value", 1);
        }
        return map;
    }


    /**
     * 根据部门ID查询用户
     */
    @RequestMapping("loadUsersByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(deptid!=null, "deptid", deptid);
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        queryWrapper.eq("type", Constant.USER_TYPE_NORMAL);
        List<User> list = this.userService.list(queryWrapper);
        return new DataGridView(list);
    }


    /**
     * 把用户名转成拼音
     */
    @RequestMapping("changeChineseToPinyin")
    public Map<String,Object> changeChineseToPinyin(String username){
        Map<String,Object> map=new HashMap<>();
        if(null!=username) {
            map.put("value", PinyinUtils.getPingYin(username));
        }else {
            map.put("value", "");
        }
        return map;
    }

    /**
     * 添加用户
     */
    @RequestMapping("addUser")
    public ResultObject addUser(UserVo userVo) {
        try {
            userVo.setType(Constant.USER_TYPE_NORMAL);//设置类型
            userVo.setHiredate(new Date());
            String salt= IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);//设置盐
            userVo.setPwd(new Md5Hash(Constant.USER_DEFAULT_PWD, salt, 2).toString());//设置密码
            this.userService.save(userVo);
            return ResultObject.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.ADD_ERROR;
        }
    }


    /**
     * 根据用户ID查询一个用户
     */
    @RequestMapping("loadUserById")
    public DataGridView loadUserById(Integer id) {
        return new DataGridView(this.userService.getById(id));
    }

    /**
     * 修改用户
     */
    @RequestMapping("updateUser")
    public ResultObject updateUser(UserVo userVo) {
        try {
            this.userService.updateById(userVo);
            return ResultObject.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.UPDATE_ERROR;
        }
    }

    /**
     * 删除用户
     */
    @RequestMapping("deleteUser")
    public ResultObject deleteUser(Integer id) {
        try {
            this.userService.removeById(id);
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }
    }
    /**
     * 重置用户密码
     */
    @RequestMapping("resetPwd")
    public ResultObject resetPwd(Integer id) {
        try {
            User user=new User();
            user.setId(id);
            String salt=IdUtil.simpleUUID().toUpperCase();
            user.setSalt(salt);//设置盐
            user.setPwd(new Md5Hash(Constant.USER_DEFAULT_PWD, salt, 2).toString());//设置密码
            this.userService.updateById(user);
            return ResultObject.RESET_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.RESET_ERROR;
        }
    }


    /**
     * 根据用户ID查询角色并选中已拥有的角色
     */
    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id) {
        //1,查询所有可用的角色
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        List<Map<String, Object>> listMaps = this.roleService.listMaps(queryWrapper);

        //2,查询当前用户拥有的角色ID集合
        List<Integer> currentUserRoleIds=this.roleService.queryUserRoleIdsByUid(id);
        for (Map<String, Object> map : listMaps) {
            Boolean LAY_CHECKED=false;
            Integer roleId=(Integer) map.get("id");
            for (Integer rid : currentUserRoleIds) {
                if(rid==roleId) {
                    LAY_CHECKED=true;
                    break;
                }
            }
            map.put("LAY_CHECKED", LAY_CHECKED);
        }
        return new DataGridView(Long.valueOf(listMaps.size()), listMaps);
    }

    /**
     * 保存用户和角色的关系
     */
    @RequestMapping("saveUserRole")
    public ResultObject saveUserRole(Integer uid,Integer[] ids) {
        try {
            this.userService.saveUserRole(uid,ids);
            return ResultObject.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DISPATCH_ERROR;
        }

    }

}

