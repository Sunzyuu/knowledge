package com.github.forest.service.impl;

import com.github.forest.entity.Tag;
import com.github.forest.mapper.TagMapper;
import com.github.forest.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
