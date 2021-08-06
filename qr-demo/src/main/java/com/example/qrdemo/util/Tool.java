package com.example.qrdemo.util;

/**
 * @author qzz
 */
public class Tool {


    /**
     * 检测字符串是否为空(null,"","null")
     * @param s
     * @return 为空返回true，否则返回false
     */
    public static boolean isEmpty(String s){
        return s == null || "".equals(s) || "null".equals(s);
    }

    /**
     * 检测字符串是否不为空(null,"","null")
     * @param s
     * @return 不为空返回true，否则返回false
     */
    public static boolean notEmpty(String s){
        return s != null && !"".equals(s) && !"null".equals(s);
    }
}
