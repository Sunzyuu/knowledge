package com.github.forest.dto;

import lombok.Data;

/**
 * @Author sunzy
 * @Date 2023/5/29 10:25
 */
@Data
public class UserRegisterInfoDTO {

    private String email;

    private String password;

    private String code;

}