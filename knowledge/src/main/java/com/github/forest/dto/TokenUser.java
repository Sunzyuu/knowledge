package com.github.forest.dto;

import lombok.Data;

import java.util.Set;

/**
 * @Author sunzy
 * @Date 2023/5/28 18:01
 */
@Data
public class TokenUser {

    private Long idUser;

    private String account;

    private String nickname;

    private String token;

    private String avatarUrl;

    private String refreshToken;

    private Set<String> scope;

    private String bankAccount;

}
