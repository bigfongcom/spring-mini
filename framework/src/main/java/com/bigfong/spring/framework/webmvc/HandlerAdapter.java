package com.bigfong.spring.framework.webmvc;

import com.bigfong.spring.framework.annotation.RequestMapping;
import com.bigfong.spring.framework.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 原生Spring的HandlerAdapter主要完成请求传递到服务商的参数列表与Method实参列表的对应关系，
 * 完成参数值的类型转换工作，核心方法是handle(),用反射来调用被适配的目标方法，并将转换包装好的参数列表传递过来
 *
 * @author bigfong
 * @since 2019/10/3
 */
public class HandlerAdapter {

    public boolean supports(HandlerMapping handler) {
        return (handler instanceof HandlerMapping);
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerMapping handlerMapping = (HandlerMapping) handler;

        //每个方法有一个参数列表，这里保存的是形参列表
        Map<String, Integer> paramMapping = new HashMap<>();

        //这里只是给出命名参数
        Annotation[][] pa = handlerMapping.getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof RequestParam) {
                    String paramName = ((RequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }

        //根据用户请求的参数信息，跟Method看的参数信息进行动态匹配
        //resp 传进来目的，将其赋值给方法参数
        //只有当用户传过来的ModelAndView为空的时候，才会新建一个默认的

        //1.要准备好这个方法的形参列表
        //方法重载时形参的决定因素：参数的个数，参数的类型，参数的顺序，方法的名称
        //只处理Request和Response
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramMapping.put(type.getName(), i);
            }
        }

        //2.得到自定义命名参数所在的位置
        //用户通过URL传来的参数列表
        Map<String, String[]> reqParameterMap = req.getParameterMap();

        //3.构造实参列表
        Object[] paramValues = new Object[paramTypes.length];
        //reqParameterMap.entrySet() 不包含HttpServletRequest、HttpServletResponse
        for (Map.Entry<String, String[]> param : reqParameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            if (!paramMapping.containsKey(param.getKey())) {
                continue;
            }

            int index = paramMapping.get(param.getKey());
            //因为页面传来的值都是String类型的，而方法中定义的类型千变万化
            //所以要针对传过来的参数进行转换
            paramValues[index] = caseStringValue(value, paramTypes[index]);
        }

        if (paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }

        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        //4.从handler中取出Controller,Method，然后利用反射机制进行调用
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if (result == null){
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
        if (isModelAndView){
            return (ModelAndView) result;
        }else{
            return null;
        }
    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz==String.class){
            return value;
        }else if (clazz==Integer.class){
            return Integer.valueOf(value);
        }else if (clazz==int.class){
            return Integer.valueOf(value).intValue();
        }else{
            return null;
        }
    }
}
