package com.github.forest.dto;

import lombok.Data;

/**
 * @Author sunzy
 * @Date 2023/5/29 10:27
 */
@Data
public class RoleDTO {

    // id
    private String id;

    // 名称
    private String name;

    // 英文名称
    private String inputCode;

    // 角色授权菜单ids
    private String menuIds;
}