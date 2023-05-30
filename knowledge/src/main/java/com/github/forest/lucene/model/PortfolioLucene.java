package com.github.forest.lucene.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PortfolioLucene
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioLucene {

    /**
     * 作品集编号
     */
    private Long idPortfolio;

    /**
     * 作品集名称
     */
    private String portfolioTitle;

    /**
     * 作品集介绍
     */
    private String portfolioDescription;

    /**
     * 相关度评分
     */
    private String score;
}
