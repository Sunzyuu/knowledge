package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.forest.core.exception.BusinessException;
import com.github.forest.entity.Article;
import com.github.forest.entity.ArticleThumbsUp;
import com.github.forest.mapper.ArticleThumbsUpMapper;
import com.github.forest.service.ArticleService;
import com.github.forest.service.ArticleThumbsUpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 文章点赞表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class ArticleThumbsUpServiceImpl extends ServiceImpl<ArticleThumbsUpMapper, ArticleThumbsUp> implements ArticleThumbsUpService {

    @Resource
    private ArticleService articleService;

    @Override
    public int thumbsUp(ArticleThumbsUp articleThumbsUp) {
        int thumbsUpNumber = 1;
        Article article = articleService.getById(articleThumbsUp.getIdArticle());
        if(Objects.isNull(article)) {
            throw new BusinessException("数据异常，文章不存在！");
        } else {
            ArticleThumbsUp thumbsUp = getById(articleThumbsUp.getIdArticleThumbsUp());
            if(Objects.isNull(thumbsUp)) {
                // 点赞
                articleThumbsUp.setThumbsUpTime(new Date());
                save(articleThumbsUp);
            } else {
                removeById(articleThumbsUp.getIdArticleThumbsUp());
                thumbsUpNumber = -1;
            }
            // 更新文章点赞数
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(true, Article::getId, articleThumbsUp.getIdArticle());
            updateWrapper.set(true, Article::getArticleSponsorCount, thumbsUpNumber);
            articleService.update(updateWrapper);
            return thumbsUpNumber;
        }
    }
}
