package com.github.forest.util;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.Date;

/**
 * @Author sunzy
 * @Date 2023/5/28 18:12
 */
public class Utils {

    public static final String UNKOWN = "unknown";

    /**
     * 生成6位数的随机验证码
     * @return
     */
    public static Integer genCode(){
        Integer code = (int) ((Math.random() * 9 - 1) * 100000);
        return code;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }


    public static String getTimeAgo(Date date) {

        String timeAgo;

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate oldLocalDate = localDateTime.toLocalDate();

        LocalDate today = LocalDate.now();

        Period p = Period.between(oldLocalDate, today);
        if (p.getYears() > 0) {
            timeAgo = p.getYears() + " 年前 ";
        } else if (p.getMonths() > 0) {
            timeAgo = p.getMonths() + " 月前 ";
        } else if (p.getDays() > 0) {
            timeAgo = p.getDays() + " 天前 ";
        } else {
            long to = System.currentTimeMillis();
            long from = date.getTime();
            int hours = (int) ((to - from) / (1000 * 60 * 60));
            if (hours > 0) {
                timeAgo = hours + " 小时前 ";
            } else {
                int minutes = (int) ((to - from) / (1000 * 60));
                if (minutes == 0) {
                    timeAgo = " 刚刚 ";
                } else {
                    timeAgo = minutes + " 分钟前 ";
                }
            }
        }
        return timeAgo;
    }
}
