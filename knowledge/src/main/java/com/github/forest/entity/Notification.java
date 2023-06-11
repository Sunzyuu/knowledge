package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通知表 
 * </p>
 *
 * @author sunzy
 * @since 2023-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long idUser;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 是否已读
     */
    private String hasRead;

    /**
     * 数据摘要
     */
    private String dataSummary;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


}
