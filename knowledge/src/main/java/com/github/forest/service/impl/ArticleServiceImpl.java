package com.github.forest.service.impl;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.ArticleSearchDTO;
import com.github.forest.entity.Article;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.service.TagService;
import com.github.forest.service.UserService;
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
        // todo::全文搜索引擎
//        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public List<ArticleDTO> findArticlesByTagName(String name) {
        return articleMapper.selectArticlesByTagName(name);
    }


}
