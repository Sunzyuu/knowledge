package com.github.forest.service.impl;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.UserInfoDTO;
import com.github.forest.dto.admin.Dashboard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author sunzy
 * @date 2023/6/27 10:29
 */
@Service
public class DashboardServiceImpl implements DashboardService{
    @Override
    public Dashboard dashboard() {
        return null;
    }

    @Override
    public Map lastThirtyDaysData() {
        return null;
    }

    @Override
    public Map history() {
        return null;
    }

    @Override
    public List<UserInfoDTO> newUsers() {
        return null;
    }

    @Override
    public List<ArticleDTO> newArticles() {
        return null;
    }
}
