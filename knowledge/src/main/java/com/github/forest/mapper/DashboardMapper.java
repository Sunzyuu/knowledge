package com.github.forest.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author sunzy
 * @date 2023/6/27 10:30
 */
@Mapper
public interface DashboardMapper {

    /**
     * 获取总用户数
     *
     * @return
     */
    Integer selectUserCount();

    /**
     * 获取新注册用户数
     *
     * @return
     */
    Integer selectNewUserCount();

    /**
     * 获取文章总数
     *
     * @return
     */
    Integer selectArticleCount();

}
