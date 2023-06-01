package com.github.forest.service;

import com.github.forest.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface TagService extends IService<Tag> {

    Tag saveTag(Tag tag);

}
