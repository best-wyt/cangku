package com.wang.yun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.yun.mapper.LoginfoMapper;
import com.wang.yun.pojo.Loginfo;
import com.wang.yun.service.LoginfoService;
import org.springframework.stereotype.Service;


@Service
public class LoginfoServiceImpl extends ServiceImpl<LoginfoMapper, Loginfo> implements LoginfoService {

}
