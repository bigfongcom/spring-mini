package com.bigfong.spring.framework.utils;

/**
 * @author bigfong
 * @since 2019/10/3
 */
public class StringUtil {

    //将类名首字母改为小写
    public static String toLowerFirstClass(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //因为大小写字母的ASCII码相差32,且大写字母的ASCII小于小写的
        //对char做算术运算，实际是对ASCII码算术运算
        chars[0]+=32;
        return String.valueOf(chars);
    }
}
