package com.bigfong.spring.framework.webmvc;

import java.util.Map;

/**
 * 封装页面模板和往页面传送的参数的对应关系
 * @author bigfong
 * @since 2019/10/3
 */
public class ModelAndView {
    private String viewName;//页面模板的名称
    private Map<String,?> model;//往页面传送的参数

    public ModelAndView(String viewName) {
        this(viewName,null);
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
