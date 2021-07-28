package com.wang.yun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("/sys")
public class SystemController {

    @RequestMapping({"/toLogin","/","login"})
    public String toLogin(){

        return "system/index/login";
    }

    @RequestMapping("/index")
    public String index(){

        return "system/index/index";
    }

    @RequestMapping("/toDeskManager")
    public String toDeskManager(){
        return "system/index/deskManager";
    }

    @RequestMapping("/toLoginfoManager")
    public String toLoginfoManager(){
        return "system/loginfo/loginfoManager";
    }


    //这是注释
    @RequestMapping("/toNoticeManager")
    public String toNoticeManager(){
        return "system/notice/noticeManager";
    }

    @RequestMapping("/toDeptManager")
    public String toDeptManager(){
        return "system/dept/deptManager";
    }

    @RequestMapping("/toDeptLeft")
    public String toDeptLeft(){
        return "system/dept/deptLeft";
    }

    @RequestMapping("/toDeptRight")
    public String toDeptRight(){
        return "system/dept/deptRight";
    }

    @RequestMapping("/toMenuManager")
    public String toMenuManager(){
        return "system/menu/menuManager";
    }

    @RequestMapping("/toMenuLeft")
    public String toMenuLeft(){
        return "system/menu/menuLeft";
    }

    @RequestMapping("/toMenuRight")
    public String toMenuRight(){
        return "system/menu/menuRight";
    }
    @RequestMapping("/toPermissionManager")
    public String toPermissionManager(){
        return "system/permission/permissionManager";
    }

    @RequestMapping("/toPermissionLeft")
    public String toPermissionLeft(){
        return "system/permission/permissionLeft";
    }

    @RequestMapping("/toPermissionRight")
    public String toPermissionRight(){
        return "system/permission/permissionRight";
    }

    @RequestMapping("/toRoleManager")
    public String toRoleManager(){
        return "system/role/roleManager";
    }


    @RequestMapping("/toUserManager")
    public String toUserManager(){
        return "system/user/userManager";
    }
}
