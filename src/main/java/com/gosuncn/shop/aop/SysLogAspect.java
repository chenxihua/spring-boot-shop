package com.gosuncn.shop.aop;

import com.alibaba.fastjson.JSON;
import com.gosuncn.shop.annonation.MyLog;
import com.gosuncn.shop.dao.SysLogDao;
import com.gosuncn.shop.entities.SysLog;
import com.gosuncn.shop.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author: chenxihua
 * @Date: 2019/2/25:16:50
 * @Version 1.0
 **/
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    SysLogDao sysLogDao;


    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.gosuncn.shop.annonation.MyLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        // 1:在切面方法里面获取一个request，
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        //保存日志
        SysLog sysLog = new SysLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
            sysLog.setOperation(value);//保存获取的操作
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args);
        sysLog.setParams(params);

        sysLog.setCreateDate(new Date());
        //获取用户名
        User user = (User) request.getSession().getAttribute("currentUser");

//        long lastAccessedTime = request.getSession().getLastAccessedTime();
//        long creationTime = request.getSession().getCreationTime();
//
//        log.info("最早访问时间： "+creationTime+"; "+"最后访问时间： "+lastAccessedTime);

        if (user!=null){
            sysLog.setUsername(user.getRealName());
        }else{
            sysLog.setUsername("异常登录");
        }

        sysLog.setUri(request.getRequestURL().toString());

        //获取用户ip地址
        sysLog.setIp(request.getRemoteHost());

        //调用service保存SysLog实体类到数据库
        sysLogDao.save(sysLog);
    }


}
