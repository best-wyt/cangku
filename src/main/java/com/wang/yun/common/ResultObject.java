package com.wang.yun.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultObject {

    public static final ResultObject  LOGIN_SUCCESS=new ResultObject(Constant.OK,"登陆成功！");
    public static final ResultObject  LOGIN_ERROR_PASS=new ResultObject(Constant.ERROR,"用户名或密码错误！");
    public static final ResultObject  LOGIN_ERROR_CODE=new ResultObject(Constant.ERROR,"验证码错误！");

    public static final ResultObject  DELETE_SUCCESS=new ResultObject(Constant.OK,"删除成功！");
    public static final ResultObject  DELETE_ERROR=new ResultObject(Constant.ERROR,"删除失败！");

    public static final ResultObject  UPDATE_SUCCESS=new ResultObject(Constant.OK,"更新成功！");
    public static final ResultObject  UPDATE_ERROR=new ResultObject(Constant.ERROR,"更新失败！");

    public static final ResultObject  ADD_SUCCESS=new ResultObject(Constant.OK,"添加成功！");
    public static final ResultObject  ADD_ERROR=new ResultObject(Constant.ERROR,"添加失败！");

    public static final ResultObject  RESET_SUCCESS=new ResultObject(Constant.OK,"重置成功！");
    public static final ResultObject  RESET_ERROR=new ResultObject(Constant.ERROR,"重置失败！");

    public static final ResultObject  DISPATCH_SUCCESS=new ResultObject(Constant.OK,"分配成功！");
    public static final ResultObject  DISPATCH_ERROR=new ResultObject(Constant.ERROR,"分配失败！");

    public static final ResultObject  OPERATE_SUCCESS=new ResultObject(Constant.OK, "操作成功");
    public static final ResultObject  OPERATE_ERROR=new ResultObject(Constant.ERROR, "操作失败");


    private Integer code;
    private String msg;
}
