package com.github.forest.dto;

import lombok.Data;

/**
 * @Author sunzy
 * @Date 2023/5/28 13:30
 */
@Data
public class UserDTO {

    private Long id;

    private String account;

    private String avatarType;

    private String avatarUrl;

    private String nickname;

    private String signature;

    private String bgImgUrl;
}
