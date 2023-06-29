package com.github.forest.service;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.UserInfoDTO;
import com.github.forest.dto.admin.Dashboard;

import java.util.List;
import java.util.Map;

/**
 * 数据面板，统计网站的数据
 * @author sunzy
 * @date 2023/6/27 10:25
 */
public interface DashboardService {
    Dashboard dashboard();


    Map lastThirtyDaysData();

    Map history();

    List<UserInfoDTO> newUsers();

    /**
     * todo::
     */

    List<ArticleDTO> newArticles();
}
