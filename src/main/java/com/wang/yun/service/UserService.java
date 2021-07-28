package com.wang.yun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.yun.pojo.User;


public interface UserService extends IService<User> {

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    void saveUserRole(Integer uid, Integer[] ids);
}
