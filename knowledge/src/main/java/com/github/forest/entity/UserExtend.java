package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户扩展表 
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_user_extend")
public class UserExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    private Long idUser;

    /**
     * github
     */
    private String github;

    /**
     * 微博
     */
    private String weibo;

    /**
     * 微信
     */
    private String weixin;

    /**
     * qq
     */
    private String qq;

    /**
     * 博客
     */
    private String blog;


}
