package com.github.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * <p>
 * 
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long idRole;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 拼音码
     */
    @Column(name = "input_code")
    private String inputCode;

    /**
     * 权重
     */
    @Column(name = "weights")
    private Integer weights;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
