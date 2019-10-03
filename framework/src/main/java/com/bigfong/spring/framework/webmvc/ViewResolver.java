package com.bigfong.spring.framework.webmvc;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 主要完成模板名称和模板解析引擎匹配
 * 1.将一个静态文件变为一个动态文件
 * 2.根据用户传送不同的参数，生产不同的结果
 * 最终输出的字符串，交给Response输出
 * @author bigfong
 * @since 2019/10/3
 */
public class ViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    private File templateRootDir;
    private String viewName;

    public ViewResolver(String templateRoot) {
        String tempateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(tempateRootPath);
    }

    public View resolveViewName(String viewName, Object o) throws Exception{
        this.viewName = viewName;
        if (null == viewName || "".equals(viewName.trim())){
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)?viewName:(viewName+DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath()+"/"+viewName).replaceAll("/+","/"));
        return new View(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
