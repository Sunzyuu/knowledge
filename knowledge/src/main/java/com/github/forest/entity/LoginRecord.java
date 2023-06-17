package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 登录记录表
 * </p>
 *
 * @author sunzy
 * @since 2023-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_login_record")
public class LoginRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户表主键
     */
    private Long idUser;

    /**
     * 登录设备IP
     */
    private String loginIp;

    /**
     * 登录设备UA
     */
    private String loginUa;

    /**
     * 登录设备所在城市
     */
    private String loginCity;

    /**
     * 登录设备操作系统
     */
    private String loginOs;

    /**
     * 登录设备浏览器
     */
    private String loginBrowser;

    /**
     * 登录时间
     */
    private Date createdTime;

    /**
     * 登录设备/浏览器指纹
     */
    private String loginDeviceId;


}
