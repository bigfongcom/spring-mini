package com.bigfong.spring.framework.webmvc.servlet;

import com.bigfong.spring.framework.annotation.Controller;
import com.bigfong.spring.framework.annotation.RequestMapping;
import com.bigfong.spring.framework.context.ApplicationContext;
import com.bigfong.spring.framework.webmvc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mvc启动入口
 *
 * @author bigfong
 * @since 2019/10/2
 */
public class DispatcherServlet extends WebServlet {
    private Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    /**
     * 主要完成IOC容器的初始化和SpringMVC九大组件的初始化
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //相当于把IOC容器初始化了
        context = new ApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

}
