package com.bigfong.spring.framework;

import com.bigfong.spring.framework.context.ApplicationContext;
import com.bigfong.spring.framework.webmvc.HandlerAdapter;
import com.bigfong.spring.framework.webmvc.HandlerMapping;
import com.bigfong.spring.framework.webmvc.ViewResolver;
import com.bigfong.spring.framework.webmvc.server.jetty.JettyServer;
import com.bigfong.spring.framework.webmvc.servlet.DispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 应用启动类
 * @Author Bigfong
 * @Date 2019/10/7
 **/
public class StartApplication {
    private Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String LOCATION = "contextConfigLocation";
    /**
     * 设计经典,核心的设计
     */
    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    private ApplicationContext context;
    
    /**
     * 启动服务
     * @param demoApplicationClass
     * @param args
     */
    public static void run(Class<?> primarySource, String[] args) {
        /*try {
            initContext(primarySource); // 依赖注入
            RequestScanner.initMapping(); // 初始化HTTP请求映射
            // 获取服务器配置
            Map<String, Object> config = ConfigUtil.getConfig();
            String server = (String) config.get("server");
            int port = (Integer) config.get("port");
            // 启动HTTP服务器
            if ("jetty".equals(server)) {
                JettyServer.start(port);
            *//*else if ("nio".equals(server))
                NioServer.start(port);*//*
            }else {
                throw new RuntimeException("Unknown server type [" + server + "]");
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
    }

    private static void initContext(Class<?> primarySource) {
    }
}
