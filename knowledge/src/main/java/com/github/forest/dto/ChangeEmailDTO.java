package com.github.forest.dto;

import lombok.Data;

/**
 * @Author sunzy
 * @Date 2023/5/29 10:28
 */
@Data
public class ChangeEmailDTO {

    private Long idUser;

    private String email;

    private String code;

}
