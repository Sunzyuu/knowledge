package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户权限表 
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    private Long idUser;

    /**
     * 角色表主键
     */
    private Long idRole;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


}
