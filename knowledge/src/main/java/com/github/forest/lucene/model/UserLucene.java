package com.github.forest.lucene.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserLucene

 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLucene {

    /**
     * 用户编号
     */
    private Long idUser;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 签名
     */
    private String signature;

    /**
     * 相关度评分
     */
    private String score;
}
