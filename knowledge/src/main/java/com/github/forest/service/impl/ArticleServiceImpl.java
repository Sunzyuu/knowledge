package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.ArticleSearchDTO;
import com.github.forest.dto.ArticleTagDTO;
import com.github.forest.dto.Author;
import com.github.forest.entity.Article;
import com.github.forest.entity.ArticleContent;
import com.github.forest.entity.Tag;
import com.github.forest.entity.User;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.service.ArticleContentService;
import com.github.forest.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.service.TagService;
import com.github.forest.service.UserService;
import com.github.forest.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagService tagService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleContentService articleContentService;

    @Value("${resource.domain}")
    private String domain;


    private static final int MAX_PREVIEW = 200;
    private static final String DEFAULT_STATUS = "0";
    private static final String DEFAULT_TOPIC_URI = "news";
    private static final int ADMIN_ROLE_WEIGHTS = 2;

    /**
     * 获取文章列表
     * @param searchDTO
     * @return
     */
    @Override
    public List<ArticleDTO> findArticles(ArticleSearchDTO searchDTO) {
        List<ArticleDTO> list;
        if(StringUtils.isNotBlank(searchDTO.getTopicUri()) && !DEFAULT_TOPIC_URI.equals(searchDTO.getTopicUri())) {
            list = articleMapper.selectArticlesByTopicUri(searchDTO.getTopicUri());
        } else {
            list = articleMapper.selectArticles(searchDTO.getSearchText(), searchDTO.getTag(), searchDTO.getTopicUri());
        }
        return list;
    }

    @Override
    public ArticleDTO findArticleDTOById(Long id, Integer type) {
        ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id, type);
        if(articleDTO == null){
            return null;
        }
        // todo::添加到全文搜索引擎
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> findArticlesByTopicUri(String name) {
        List<ArticleDTO> list = articleMapper.selectArticlesByTopicUri(name);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }



    @Override
    public List<ArticleDTO> findArticlesByTagName(String name) {
        return articleMapper.selectArticlesByTagName(name);
    }





    /**
     * 生成文章的静态页面
     * @param article
     * @param type
     * @return
     */
    private ArticleDTO genArticle(ArticleDTO article, Integer type) {
        Integer articleList = 0;
        Integer articleView = 1;
        Integer articleEdit = 2;
        Author author = genAuthor(article);
        article.setTimeAgo(Utils.getTimeAgo(article.getUpdatedTime()));
        List<ArticleTagDTO> tags = articleMapper.selectTags(article.getIdArticle());
        article.setTags(tags);

        if(!type.equals(articleList)) {
            ArticleContent articleContent = articleContentService.getById(article.getIdArticle());
            if (type.equals(articleView)) {
                // todo:: 生成静态的文章页面
            } else if (type.equals(articleEdit)) {
                article.setArticleContent(articleContent.getArticleContent());
            }
        }

        return article;
    }

    private Author genAuthor(ArticleDTO article) {
        Author author = new Author();
        User user = userService.getById(article.getArticleAuthorId());
        author.setUserNickname(article.getArticleAuthorName());
        author.setUserAvatarURL(article.getArticleAuthorAvatarUrl());
        author.setIdUser(article.getArticleAuthorId());
        author.setUserAccount(user.getAccount());
        return author;
    }

    private String checkTags(String articleTags) {
        if(StringUtils.isEmpty(articleTags)) {
            return  "";
        }
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(true, Tag::getTagReservation, "1");
        List<Tag> tags = tagService.list(queryWrapper);
        if(tags.isEmpty()) {
            return "";
        } else {
            String[] articleTagArr = articleTags.split(",");

            for (Tag tag : tags) {
                if (StringUtils.isBlank(tag.getTagTitle())) {
                    continue;
                }

                for (String articleTag : articleTagArr) {
                    if(StringUtils.isBlank(articleTag)) {
                        continue;
                    }
                    if(articleTag.equals(tag.getTagTitle())) {
                        return tag.getTagTitle();
                    }

                }
            }
        }
        return "";
    }
}
