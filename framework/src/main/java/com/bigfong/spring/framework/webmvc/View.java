package com.bigfong.spring.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义模板引擎,
 * 核心方法是render, 完成对模板的渲染，最终返回浏览器能识别的字符串，通过Response输出
 *
 * @author bigfong
 * @since 2019/10/3
 */
public class View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private File viewFile;


    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    public String getContentType(){
        return DEFAULT_CONTENT_TYPE;
    }

    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception{
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile,"r");

        try {
            String line = null;
            while (null !=(line=ra.readLine())){
                line = new String(line.getBytes("ISO-8859-1"),"utf-8");
                Pattern pattern = Pattern.compile("$\\{[^\\}]+\\}",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()){
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("$\\{|\\}","");
                    Object paramValue = model.get(paramName);
                    if (null == paramValue){
                        continue;
                    }
                    //把${}中间的这个字符串取出来
                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                sb.append(line);
            }
        }finally {
            ra.close();
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType(DEFAULT_CONTENT_TYPE);
        resp.getWriter().write(sb.toString());
    }

    /**
     * 处理特殊字符串
     * @param s
     * @return
     */
    private String makeStringForRegExp(String str) {
        return str.replace("\\","\\\\").replace("*","\\*")
                .replace("+","\\+").replace("|","\\|")
                .replace("{","\\{").replace("}","\\}")
                .replace("(","\\(").replace(")","\\)")
                .replace("^","\\^")//.replace("$","\\$")
                .replace("[","\\[").replace("]","\\]")
                .replace("?","\\?").replace(",","\\,")
                .replace(".","\\.").replace("&","\\&");
    }
}
