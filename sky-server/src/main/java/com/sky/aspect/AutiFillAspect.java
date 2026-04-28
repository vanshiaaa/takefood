package com.sky.aspect;

import com.sky.anno.Autofill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutiFillAspect {
    //1.自定义切入点

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.anno.Autofill)")
    public void autoFillAspect(){}

    //2.前置通知
    @Before("autoFillAspect()")
    public void autufill(JoinPoint joinPoint) throws Exception {
        //1.获取当前被拦截的方法的数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Autofill autofill = signature.getMethod().getAnnotation(Autofill.class);
        OperationType operationType = autofill.value();
        //2.获取当前被拦截的方法的参数对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;

        }
        Object object = args[0];
        //3.准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同的操作类型进行操作 通过反射赋值
        if (operationType == OperationType.INSERT){

            Method setCreateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
            Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreatUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //通过反射赋值
            setCreateTime.invoke(object,now);
            setCreatUser.invoke(object,currentId);
            setUpdateTime.invoke(object,now);
            setUpdateUser.invoke(object,currentId);

        }else if (operationType == OperationType.UPDATE){

            Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //通过反射赋值
            setUpdateTime.invoke(object,now);
            setUpdateUser.invoke(object,currentId);


        }

    }

}
