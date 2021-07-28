package com.wang.yun.cache;

import com.wang.yun.pojo.Dept;
import com.wang.yun.pojo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@EnableAspectJAutoProxy
public class CacheAspect {

    /**
     * 日志出处
     */
    private Log log = LogFactory.getLog(CacheAspect.class);


    private static Map<String,Object> cacheContainer=new HashMap<>();

    private static final String POINTCUT_DEPT_ADD = "execution(* com.wang.yun.service.impl.DeptServiceImpl.save(..))";
    private static final String POINTCUT_DEPT_Get="execution(* com.wang.yun.service.impl.DeptServiceImpl.getById(..))";
    private static final String POINTCUT_DEPT_UPDATE="execution(* com.wang.yun.service.impl.DeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_DELETE="execution(* com.wang.yun.service.impl.DeptServiceImpl.removeById(..))";

    private static final String  CACHE_DEPT_PROFIX="dept:";


    /**
     * 部门添加切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_DEPT_ADD)
    public Object cacheDeptAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        Dept object = (Dept) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res) {
            cacheContainer.put(CACHE_DEPT_PROFIX + object.getId(), object);
        }
        return res;
    }


    @Around(value = POINTCUT_DEPT_Get)
    public Object get(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer id = (Integer) joinPoint.getArgs()[0];
        Object o = cacheContainer.get(CACHE_DEPT_PROFIX + id);
        if (o!=null){
            return o;
        }
        Dept dept = (Dept) joinPoint.proceed();//调用切入点方法
        cacheContainer.put(CACHE_DEPT_PROFIX+dept.getId(),dept);
        return dept;
    }

    @Around(value=POINTCUT_DEPT_UPDATE)
    public Object updateById(ProceedingJoinPoint joinPoint) throws Throwable {
        Dept deptVo= (Dept) joinPoint.getArgs()[0];
        Boolean flag = (Boolean) joinPoint.proceed();
        if (flag){
            Dept dept = (Dept) cacheContainer.get(CACHE_DEPT_PROFIX + deptVo.getId());
            if (dept==null){
                dept=new Dept();
            }
            BeanUtils.copyProperties(deptVo,dept);
            cacheContainer.put(CACHE_DEPT_PROFIX+deptVo.getId(),dept);
        }
        return flag;
    }

    @Around(value=POINTCUT_DEPT_DELETE)
    public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean  isSuccess =(Boolean) joinPoint.proceed();
        if(isSuccess) {
            //删除缓存
            cacheContainer.remove(CACHE_DEPT_PROFIX+id);
        }
        return isSuccess;
    }


    // 声明切面表达式
    private static final String POINTCUT_USER_UPDATE = "execution(* com.wang.yun.service.impl.UserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_ADD = "execution(* com.wang.yun.service.impl.UserServiceImpl.save(..))";
    private static final String POINTCUT_USER_GET = "execution(* com.wang.yun.service.impl.UserServiceImpl.getById(..))";
    private static final String POINTCUT_USER_DELETE = "execution(* com.wang.yun.service.impl.UserServiceImpl.removeById(..))";

    private static final String CACHE_USER_PROFIX = "user:";

    /**
     * 用户添加切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_ADD)
    public Object cacheUserAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        User object = (User) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res) {
            cacheContainer.put(CACHE_USER_PROFIX + object.getId(), object);
        }
        return res;
    }

    /**
     * 查询切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_GET)
    public Object cacheUserGet(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        // 从缓存里面取
        Object res1 = cacheContainer.get(CACHE_USER_PROFIX + object);
        if (res1 != null) {
            log.info("已从缓存里面找到用户对象" + CACHE_USER_PROFIX + object);
            return res1;
        } else {
            User res2 = (User) joinPoint.proceed();
            cacheContainer.put(CACHE_USER_PROFIX + res2.getId(), res2);
            log.info("未从缓存里面找到用户对象，去数据库查询并放到缓存"+CACHE_USER_PROFIX+res2.getId());
            return res2;
        }
    }

    /**
     * 更新切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_UPDATE)
    public Object cacheUserUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        User userVo = (User) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            User user = (User) cacheContainer.get(CACHE_USER_PROFIX + userVo.getId());
            if (null == user) {
                user = new User();
            }
            BeanUtils.copyProperties(userVo, user);
            log.info("用户对象缓存已更新" + CACHE_USER_PROFIX + userVo.getId());
            cacheContainer.put(CACHE_USER_PROFIX + user.getId(), user);
        }
        return isSuccess;
    }

    /**
     * 删除切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_DELETE)
    public Object cacheUserDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            // 删除缓存
            cacheContainer.remove(CACHE_USER_PROFIX + id);
            log.info("用户对象缓存已删除" + CACHE_USER_PROFIX + id);
        }
        return isSuccess;
    }


}
