package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 关注表 
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_follow")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long idFollow;

    /**
     * 关注者 id
     */
    private Long followerId;

    /**
     * 关注数据 id
     */
    private Long followingId;

    /**
     * 0：用户，1：标签，2：帖子收藏，3：帖子关注
     */
    private String followingType;


}
