package com.wang.yun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.ResultObject;
import com.wang.yun.common.WebUtils;
import com.wang.yun.pojo.Notice;
import com.wang.yun.pojo.User;
import com.wang.yun.service.NoticeService;
import com.wang.yun.vo.LoginfoVo;
import com.wang.yun.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/loadAllNotice")
    public DataGridView loadAllNotice(NoticeVo noticeVo){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername());
        queryWrapper.ge(noticeVo.getStartTime()!=null,"createtime",noticeVo.getStartTime());
        queryWrapper.le(noticeVo.getEndTime()!=null,"createtime",noticeVo.getEndTime());
        queryWrapper.orderByDesc("createtime");
        IPage<Notice> iPage=noticeService.page(new Page<>(noticeVo.getPage(),noticeVo.getLimit()),queryWrapper);
        return new DataGridView(iPage.getTotal(),iPage.getRecords());
    }

    @PostMapping("/addNotice")
    public ResultObject addNotice(NoticeVo noticeVo){
        try {
            noticeVo.setCreatetime(new Date());
            User user= (User) WebUtils.getSession().getAttribute("user");
            noticeVo.setOpername(user.getName());
            noticeService.save(noticeVo);
            return ResultObject.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.ADD_ERROR;
        }
    }


    @PostMapping("/updateNotice")
    public ResultObject updateNotice(NoticeVo noticeVo){
        try {
            noticeService.updateById(noticeVo);
            return ResultObject.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.UPDATE_ERROR;
        }
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteNotice")
    public ResultObject deleteNotice(Integer id){
        try {
            noticeService.removeById(id);
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     * @param noticeVo
     * @return
     */
    @PostMapping("/batchDeleteNotice")
    public ResultObject batchDeleteNotice(NoticeVo noticeVo){
        try {
            Collection<Integer> ids=new ArrayList<>();
            for (Integer id : noticeVo.getIds()) {
                ids.add(id);
            }
            noticeService.removeByIds(ids);
            return ResultObject.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObject.DELETE_ERROR;
        }
    }

}
