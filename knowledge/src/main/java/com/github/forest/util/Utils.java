package com.github.forest.util;

/**
 * @Author sunzy
 * @Date 2023/5/28 18:12
 */
public class Utils {



    public static Integer genCode(){
        Integer code = (int) ((Math.random() * 9 - 1) * 100000);
        return code;
    }
}
