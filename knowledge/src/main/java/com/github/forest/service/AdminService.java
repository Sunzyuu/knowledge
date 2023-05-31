package com.github.forest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.entity.Topic;
import com.github.pagehelper.PageInfo;

/**
 * @author sunzy
 * @date 2023/5/31 21:41
 */
public interface AdminService {
    GlobalResult<PageInfo<Topic>> getTopics(Integer page, Integer pageSize);
}
