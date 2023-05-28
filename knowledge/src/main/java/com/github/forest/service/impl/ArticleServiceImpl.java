package com.github.forest.service.impl;

import com.github.forest.entity.Article;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
