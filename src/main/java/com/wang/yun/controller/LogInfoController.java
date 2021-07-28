package com.wang.yun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.ResultObject;
import com.wang.yun.pojo.Loginfo;
import com.wang.yun.service.LoginfoService;
import com.wang.yun.vo.LoginfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/loginfo")
@CrossOrigin
public class LogInfoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 查询
     * @param loginfoVo
     * @return
     */
    @RequestMapping("/loadAllLoginfo")
    public DataGridView loadAllLoginfo(LoginfoVo loginfoVo){
        QueryWrapper<Loginfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginname()),"loginname",loginfoVo.getLoginname());
        queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()),"loginip",loginfoVo.getLoginip());
        queryWrapper.ge(loginfoVo.getStartTime()!=null,"logintime",loginfoVo.getStartTime());
        queryWrapper.ge(loginfoVo.getEndTime()!=null,"logintime",loginfoVo.getEndTime());
        queryWrapper.orderByDesc("logintime");
        IPage<Loginfo> iPage = loginfoService.page(new Page<>(loginfoVo.getPage(), loginfoVo.getLimit()), queryWrapper);
        return new DataGridView(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteLoginfo")
    public ResultObject deleteLoginfo(Integer id){
        try {
            loginfoService.removeById(id);
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     * @param loginfoVo
     * @return
     */
    @PostMapping("/batchDeleteLoginfo")
    public ResultObject batchDeleteLoginfo(LoginfoVo loginfoVo){
        try {
            Collection<Integer> ids=new ArrayList<>();
            for (Integer id : loginfoVo.getIds()) {
                ids.add(id);
            }
            loginfoService.removeByIds(ids);
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }
    }


}
