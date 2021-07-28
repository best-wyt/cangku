package com.wang.yun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.yun.mapper.NoticeMapper;
import com.wang.yun.pojo.Notice;
import com.wang.yun.service.NoticeService;
import org.springframework.stereotype.Service;


@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
