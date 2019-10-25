package com.bigfong.spring.framework.aop.support;

import com.bigfong.spring.framework.aop.AopConfig;
import com.bigfong.spring.framework.aop.aspect.AfterReturningAdvice;
import com.bigfong.spring.framework.aop.aspect.AfterThrowingAdvice;
import com.bigfong.spring.framework.aop.aspect.MethodBeforeAdvice;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要解析和封装AOP配置
 * 其中pointCutMatch()方法用来判断目标类是否符合切面规则，从而决定是否需要生成代理类，对目标方法进行增强
 * getInterceptorsAndDynamicInterceptionAdvice()方法主要根据AOP配置，将需要回调的方法封装成一个拦截链并返回提供给外部获取
 * @Author Bigfong
 * @Date 2019/10/4
 **/
public class AdvisedSupport {
    private Class targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;
    private AopConfig config;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?> targetClass) throws Exception{
        List<Object> cached = this.methodCache.get(method);
        //未缓存
        if (null == cached){
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            cached = methodCache.get(m);
            /*cached = new ArrayList<>();
            cached.add(m);*/
            if (null != cached){
                //存入缓存
                this.methodCache.put(m,cached);
            }
        }
        return cached;
    }

    private void parse() {
        //pointCut表达式
        String pointCut = config.getPointCut()
                .replaceAll("\\.","\\\\.")
                .replaceAll("\\\\.\\*",".*")
                .replaceAll("\\(","\\\\(")
                .replaceAll("\\)","\\\\)");

        String pointCutForClasss = pointCut.substring(0,pointCut.lastIndexOf("\\(")-4);
        pointCutClassPattern = Pattern.compile("class "+pointCutForClasss.substring(pointCutForClasss.lastIndexOf(" ")+1));

        methodCache = new HashMap<>();
        Pattern pattern = Pattern.compile(pointCut);

        try {
            Class aspectClass = Class.forName(config.getAspectClass());
            Map<String,Method> aspectMethods = new HashMap<>();
            for(Method m: aspectClass.getMethods()){
                aspectMethods.put(m.getName(),m);
            }

            //这里得到的方法都是原生方法
            for (Method m : targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")){
                    methodString = methodString.substring(0,methodString.indexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()){
                    //满足切面规则的类，添加到AOP配置中
                    List<Object> advice = new LinkedList<>();
                    //前置通知
                    if (!(null==config.getAspectBefore() || "".equals(config.getAspectBefore().trim())) ){
                        advice.add(new MethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()),aspectClass.newInstance()));
                    }

                    //后置通知
                    if (!(null==config.getAspectAfter() || "".equals(config.getAspectAfter().trim())) ){
                        advice.add(new AfterReturningAdvice(aspectMethods.get(config.getAspectAfter()),aspectClass.newInstance()));
                    }

                    //后置通知
                    if (!(null==config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow().trim())) ){
                        AfterThrowingAdvice afterThrowingAdvice = new AfterThrowingAdvice(aspectMethods.get(config.getAspectAfterThrow()),aspectClass.newInstance());
                        afterThrowingAdvice.setThrowingName(config.getAspectAfterThrowingName());
                        advice.add(afterThrowingAdvice);
                    }

                    if (null == advice || advice.isEmpty()){
                        continue;
                    }

                    methodCache.put(m,advice);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }
}
