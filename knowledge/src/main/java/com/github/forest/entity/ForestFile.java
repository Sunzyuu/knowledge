package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件上传记录表
 * </p>
 *
 * @author sunzy
 * @since 2023-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_file")
public class ForestFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件md5值
     */
    private String md5Value;

    /**
     * 文件上传路径
     */
    private String filePath;

    /**
     * 网络访问路径
     */
    private String fileUrl;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;


}
