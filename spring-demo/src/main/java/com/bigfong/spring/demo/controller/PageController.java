package com.bigfong.spring.demo.controller;

import com.bigfong.spring.demo.service.IModifyService;
import com.bigfong.spring.demo.service.IQueryService;
import com.bigfong.spring.framework.annotation.Autowired;
import com.bigfong.spring.framework.annotation.Controller;
import com.bigfong.spring.framework.annotation.RequestMapping;
import com.bigfong.spring.framework.annotation.RequestParam;
import com.bigfong.spring.framework.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 演示模板
 * @author bigfong
 * @since 2019/10/3
 */
@Controller
@RequestMapping("/page")
public class PageController {
    @Autowired
    private IQueryService queryService;
    @Autowired
    IModifyService modifyService;

    @RequestMapping("/first.html")
    public ModelAndView query(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name){
        String result = queryService.query(name);
        Map<String,Object> model = new HashMap<>();
        model.put("name",name);
        model.put("data",result);
        model.put("token","123456");
        return new ModelAndView("first.html",model);
    }

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name, @RequestParam("addr") String addr)throws Throwable{
        String result = null;
        Map<String,Object> model = new HashMap<>();
        /*try {
            result = modifyService.add(name,addr);
            response.getWriter().write(result);
            return null;
        } catch (Exception e) {
            model.put("detail",e.getCause());

            model.put("stackTrace","");//System.err
            return new ModelAndView("500.html",model);
        }*/

        result = modifyService.add(name,addr);
        response.getWriter().write(result);
        return null;
    }
}
