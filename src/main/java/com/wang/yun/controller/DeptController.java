package com.wang.yun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.ResultObject;
import com.wang.yun.common.TreeNode;
import com.wang.yun.pojo.Dept;
import com.wang.yun.pojo.Notice;
import com.wang.yun.service.DeptService;
import com.wang.yun.vo.DeptVo;
import com.wang.yun.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin
@RequestMapping("/dept")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("checkDeptHasChildrenNode")
    public Map<String,Object> checkDeptHasChildrenNode(DeptVo deptVo){
        Map<String,Object> map=new HashMap<>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",deptVo.getId());
        List<Dept> list = deptService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    @RequestMapping("getMaxOrderNum")
    public Integer getMaxOrderNum(){
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("ordernum");
        List<Dept> list = deptService.list(wrapper);
        if (list.size()>0){
            return list.get(0).getOrdernum()+1;
        }else {
            return 1;
        }


    }

    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo){
        List<Dept> list = deptService.list();
        List<TreeNode> nodes = new ArrayList<>();
        for (Dept dept : list) {
            Boolean spread=dept.getOpen()==1?true:false;
            nodes.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),spread));
        }
        return new DataGridView(nodes);
    }


    @RequestMapping("/loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo){
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()),"title",deptVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()),"address",deptVo.getAddress());
        queryWrapper.ge(deptVo.getRemark()!=null,"remark",deptVo.getRemark());
        queryWrapper.eq(deptVo.getId()!=null,"id",deptVo.getId()).or().eq(deptVo.getId()!=null,"pid", deptVo.getId());
        queryWrapper.orderByAsc("ordernum");
        IPage<Dept> iPage=deptService.page(new Page<>(deptVo.getPage(),deptVo.getLimit()),queryWrapper);
        return new DataGridView(iPage.getTotal(),iPage.getRecords());
    }


    @RequestMapping("addDept")
    public ResultObject addDept(DeptVo deptVo){
        try {
            deptVo.setCreatetime(new Date());
            deptService.save(deptVo);
            return ResultObject.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.ADD_SUCCESS;
        }

    }

    @RequestMapping("updateDept")
    public ResultObject updateDept(DeptVo deptVo){
        try {
            deptService.updateById(deptVo);
            return ResultObject.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.UPDATE_ERROR;
        }

    }

    @RequestMapping("deleteDept")
    public ResultObject deleteDept(DeptVo deptVo){
        try {
            deptService.removeById(deptVo.getId());
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }

    }


}
