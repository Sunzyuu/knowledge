package com.github.forest.dto.admin;

import lombok.Data;

/**
 * @author sunzy
 * @date 2023/6/27 10:26
 */
@Data
public class Dashboard {

    private Integer countUserNum;

    private Integer newUserNum;

    private Integer countArticleNum;

    private Integer newArticleNum;

    private Integer countViewNum;

    private Integer todayViewNum;
}
