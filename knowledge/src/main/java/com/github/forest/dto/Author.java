package com.github.forest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author sunzy
 * @Date 2023/5/29 10:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private Long idUser;

    private String userNickname;

    private String userAccount;

    private String userAvatarURL;

    private String userArticleCount;

}