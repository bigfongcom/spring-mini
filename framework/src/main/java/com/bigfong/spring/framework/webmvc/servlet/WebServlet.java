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
public abstract class WebServlet extends HttpServlet {
    protected Logger logger = LoggerFactory.getLogger(WebServlet.class);

    protected final String LOCATION = "contextConfigLocation";
    /**
     * 设计经典,核心的设计
     */
    protected List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    protected Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<>();

    protected List<ViewResolver> viewResolvers = new ArrayList<>();

    protected ApplicationContext context;

    /**
     * 主要完成IOC容器的初始化和SpringMVC九大组件的初始化
     *
     * @param config
     * @throws ServletException
     */
   /* @Override
    public void init(ServletConfig config) throws ServletException {
        //相当于把IOC容器初始化了
        context = new ApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }*/

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * 有九种策略
     * 针对每一个用户请求，都会经过一些处理策略处理，最终才能有结果输出
     * 每种策略可以自定义干预，但是最终的结果都一致
     *
     * @param context
     */
    protected void initStrategies(ApplicationContext context) {
        //===============SpringMVC中的九大组件======================//
        //1.文件上传解析，如果 请求类型是multipart,将通过initMultipartResolver进行文件上传解析
        initMultipartResolver(context);
        //2.本地化解析
        initLocaleResolver(context);
        //3.主题解析
        initThemeResolver(context);
        //4.通过HandlerMapper将请求映射到处理器上
        //HandlerMapping用来保存Controller中配置的RequestMapping和Method人对应关系
        initHandlerMappings(context);
        //5.通过HandlerAdapter进行多类型的参数动态匹配
        //HandlerAdapter用来动态匹配Method,包括类型转换、动态赋值
        initHandlerAdapter(context);
        //6.异常处理
        //如果执行过程中遇到异常，将交给HandlerExceptionResolver来解析
        initHandlerExceptionResolver(context);
        //7.将请求解析到视图名
        initRequestToViewNameTranslator(context);
        //8.动态模板解析，将逻辑视图解析到具体视图实现
        initViewResolver(context);
        //9.Flash映射管理器
        initFlashMapManager(context);
    }

    /**
     * 文件上传解析，如果 请求类型是multipart,将通过initMultipartResolver进行文件上传解析
     *
     * @param context
     */
    private void initMultipartResolver(ApplicationContext context) {
    }

    /**
     * 本地化解析
     *
     * @param context
     */
    private void initLocaleResolver(ApplicationContext context) {
    }

    /**
     * 主题解析
     *
     * @param context
     */
    private void initThemeResolver(ApplicationContext context) {
    }

    /**
     * HandlerMapping用来保存Controller中配置的RequestMapping和Method人对应关系
     *
     * @param context
     */
    private void initHandlerMappings(ApplicationContext context) {
        //首先从容器中获取所有的实例
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                //在MVC层，对旬提供的方法只有一个betBean方法
                //返回的对象不是Wreaaper,怎么处理？
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                //扫描所有的public类型的方法
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + baseUrl + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new HandlerMapping(pattern, controller, method));
                    logger.info("Mapping:" + regex + "," + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过HandlerAdapter进行多类型的参数动态匹配
     *
     * @param context
     */
    private void initHandlerAdapter(ApplicationContext context) {
        //在初始化阶段，将这些参数的名字或者类型按一定的顺序保存下来
        //因为后面用反射调用的时候，传的形参选是一个数组
        //可以通过记录这些参数的位置Index,逐个从数组中取值，这样就和参数的顺序无关了
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            //每个方法有一个参数列表，这里保存的是形参选列表
            this.handlerAdapters.put(handlerMapping,new HandlerAdapter());
        }
    }

    /**
     * 如果执行过程中遇到异常，将交给HandlerExceptionResolver来解析
     *
     * @param context
     */
    private void initHandlerExceptionResolver(ApplicationContext context) {
    }

    /**
     * 将请求解析到视图名
     *
     * @param context
     */
    private void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    /**
     * 动态模板解析，将逻辑视图解析到具体视图实现
     *
     * @param context
     */
    private void initViewResolver(ApplicationContext context) {
        //在页面输入http://localhost/xxx.html
        //解决页面名字和模板文件关联的问题
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String tempateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(tempateRootPath);
        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }
    }

    /**
     * Flash映射管理器
     *
     * @param context
     */
    private void initFlashMapManager(ApplicationContext context) {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        }catch (Exception e ){
            view500(req,resp,e);
        }
    }

    private void view500(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        //判断500文件是否存在

         /*resp.getWriter().write("<font size='25' color='blue'>500 Exception</font><br>Detail:<br/>"+
                    Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]]","").replaceAll("\\s","\r\n")+
            "<font color='greeb'><br><i>Copyright@bigfong</i></font>");
            e.printStackTrace();*/
        try {
            Map<String,Object> model = new HashMap<>();
            model.put("detail",e.getCause());
            model.put("stackTrace",Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]]","").replaceAll("\\s","\r\n"));//System.err
            processDispatchResult(req,resp,new ModelAndView("500",model));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void view404(HttpServletRequest req, HttpServletResponse resp)  throws Exception{
        processDispatchResult(req,resp,new ModelAndView("404"));
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //根据用户请求的URL来获得一个Handler
        HandlerMapping handler = getHandler(req);
        if (handler==null){
            view404(req,resp);
            return;
        }
        HandlerAdapter ha = getHandlerAdapter(handler);
        //这一步只是调用方法，得到返回值
        ModelAndView mv = ha.handle(req,resp,handler);
        //输出
        processDispatchResult(req,resp,mv);
    }




    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception{
        //调用ViewResolver的resolveViewName()方法
        if (null == mv){
            return;
        }
        if (this.viewResolvers.isEmpty()){
            return;
        }
        if (this.viewResolvers!=null){
            for (ViewResolver viewResolver : this.viewResolvers) {
                View view = viewResolver.resolveViewName(mv.getViewName(),null);
                if (view!=null){
                    view.render(mv.getModel(),req,resp);
                    return;
                }
            }
        }
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()){
            return null;
        }
        HandlerAdapter ha = this.handlerAdapters.get(handler);
        if (ha.supports(handler)){
            return ha;
        }
        return null;
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()){
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");

        for (HandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()){
                continue;
            }
            return handler;
        }
        return null;
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
