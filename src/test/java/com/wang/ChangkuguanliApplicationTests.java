package com.wang;

import com.wang.yun.pojo.User;
import com.wang.yun.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChangkuguanliApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        User user = userService.getById(3);
        System.out.println(user);
    }

}
