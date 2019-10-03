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

/**
 * 测试controller
 * @author bigfong
 * @since 2019/10/3
 */

@Controller
@RequestMapping("/web")
public class TestController {
    @Autowired
    IQueryService queryService;
    @Autowired
    IModifyService modifyService;

    @RequestMapping("/query.json")
    public ModelAndView query(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name){
        String result = queryService.query(name);
        return out(response,result);
    }

    private ModelAndView out(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
