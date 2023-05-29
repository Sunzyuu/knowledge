package com.github.forest.dto;

import lombok.Data;

/**
 * @Author sunzy
 * @Date 2023/5/29 10:26
 */
@Data
public class ForgetPasswordDTO {
    private String code;

    private String password;
}
