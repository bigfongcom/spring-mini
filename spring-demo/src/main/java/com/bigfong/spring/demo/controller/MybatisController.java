package com.bigfong.spring.demo.controller;

import com.bigfong.spring.demo.service.IModifyService;
import com.bigfong.spring.demo.service.IMybatisService;
import com.bigfong.spring.demo.service.IQueryService;
import com.bigfong.spring.framework.annotation.Autowired;
import com.bigfong.spring.framework.annotation.Controller;
import com.bigfong.spring.framework.annotation.RequestMapping;
import com.bigfong.spring.framework.annotation.RequestParam;
import com.bigfong.spring.framework.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试controller
 * @author bigfong
 * @since 2019/10/16
 */

@Controller
@RequestMapping("/mybatis")
public class MybatisController {

    @Autowired
    IMybatisService yybatisService;

    @RequestMapping("/test")
    public ModelAndView query(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name){
        try {
            yybatisService.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
