package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.forest.core.exception.BusinessException;
import com.github.forest.core.exception.ContentNotExistException;
import com.github.forest.core.exception.UltraViresException;
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
import com.github.forest.util.Html2TextUtil;
import com.github.forest.util.Utils;
import com.github.forest.util.XssUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文章表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
@Lazy
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
        genArticle(articleDTO, type);
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

    @Override
    public List<ArticleDTO> findUserArticlesByIdUser(Long idUser) {
        List<ArticleDTO> list = articleMapper.selectUserArticles(idUser);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    @Transactional(rollbackFor =  {UnsupportedEncodingException.class})
    public Long postArticle(ArticleDTO article, User user) throws UnsupportedEncodingException {
        boolean isUpdated = false;
        String articleTitle = article.getArticleTitle();
        String articleTags = article.getArticleTags();
        String articleContent = article.getArticleContent();
        String articleContentHtml = XssUtils.filterHtmlCode(article.getArticleContentHtml());
        String reservedTag = checkTags(articleTags);
        boolean notification = false;
        if(StringUtils.isNotBlank(reservedTag)) {
            Integer roleWeight = userService.findRoleWeightsByUser(user.getId());
            if(roleWeight > ADMIN_ROLE_WEIGHTS) {
                throw new UltraViresException(StringEscapeUtils.unescapeJava(reservedTag) + "标签为系统保留标签!");
            } else {
                notification = true;
            }
        }
        Article newArticle;
        Long idArticle = article.getIdArticle();
        if(idArticle == null || idArticle == 0) {
            newArticle = new Article();
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleAuthorId(user.getId());
            newArticle.setArticleTags(articleTags);
            newArticle.setCreatedTime(new Date());
            newArticle.setUpdatedTime(newArticle.getCreatedTime());
            newArticle.setArticleStatus(article.getArticleStatus());
            save(newArticle);
            articleMapper.insertArticleContent(newArticle.getId(), articleContent, articleContentHtml);
        } else {
            newArticle = getById(idArticle);
            // 如果文章之前状态为草稿 应视为新发布文章
            if (DEFAULT_STATUS.equals(article.getArticleStatus())) {
                isUpdated = true;
            }
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleTags(articleTags);
            newArticle.setArticleStatus(article.getArticleStatus());
            newArticle.setUpdatedTime(new Date());
            updateById(newArticle);
        }
        Long newArticleId = newArticle.getId();
        // 更新文章链接
        if(DEFAULT_STATUS.equals(newArticle.getArticleStatus())) {
            // 文章
            newArticle.setArticlePermalink(domain + "/article/" + newArticleId);
            newArticle.setArticleLink("/article/" + newArticleId);
        } else {
            // 草稿
            newArticle.setArticlePermalink(domain + "/draft/" + newArticleId);
            newArticle.setArticleLink("/draft/" + newArticleId);
        }

        if(StringUtils.isNotBlank(articleContentHtml)) {
            String previewContent = Html2TextUtil.getContent(articleContent);
            // 生成200字的预览信息
            if(previewContent.length() > MAX_PREVIEW) {
                previewContent = previewContent.substring(0, MAX_PREVIEW);
            }
            newArticle.setArticlePreviewContent(previewContent);
        }
        updateById(newArticle);
        // 插入标签信息
        tagService.saveTagArticle(newArticle, articleContentHtml, user.getId());
        // todo:: 事件监听器

        return newArticleId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Long id) {
        // 首先判断是否有相关评论
        if(!articleMapper.existsCommentWithPrimaryKey(id)) {
            // 删除相关数据 作品集关联关系 标签关联关系等

            // 删除文章
            boolean result = removeById(id);
            if(!result) {
                // todo::事件监听器
            }
            return 1;
        } else {
            throw new BusinessException("已有评论的文章不允许删除！");
        }
    }

    private void deleteLinkedData(Long id) {
        // 删除关联的作品集
        articleMapper.deleteLinkedPortfolioData(id);
        // 删除引用标签记录
        articleMapper.deleteTagArticle(id);
        // 删除文章内容表
        articleMapper.deleteArticleContent(id);
        // todo:: 删除未读消息
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementArticleViewCount(Long id) {
        Article article = getById(id);
        int articleViewCount = article.getArticleViewCount() + 1;
        article.setArticleViewCount(articleViewCount);
        updateById(article);
    }

    @Override
    public String share(Integer id, String account) {
        Article article = getById(id);
        return article.getArticlePermalink() + "?s=" + account;
    }

    @Override
    public List<ArticleDTO> findDrafts(Long userId) {
        List<ArticleDTO> list = articleMapper.selectDrafts(userId);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public List<ArticleDTO> findArticlesByIdPortfolio(Long idPortfolio) {
        List<ArticleDTO> list = articleMapper.selectArticlesByIdPortfolio(idPortfolio);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

//    @Override
//    public Long postArticle(ArticleDTO article, User user) throws UnsupportedEncodingException {
//        return null;
//    }

    @Override
    public List<ArticleDTO> selectUnbindArticles(Long idPortfolio, String searchText, Long idUser) {
        List<ArticleDTO> list = articleMapper.selectUnbindArticlesByIdPortfolio(idPortfolio, searchText, idUser);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public Boolean updateTags(Long idArticle, String tags, Long userId) throws UnsupportedEncodingException {
        Article article = getById(idArticle);
        if(Objects.isNull(article)) {
            throw new ContentNotExistException("更新失败，文章不存在");
        }
        article.setArticleTags(tags);
//        articleMapper.updateArticleTags(idArticle, tags);
        updateById(article);
        tagService.saveTagArticle(article, "", userId);
        return null;
    }

    @Override
    public Boolean updatePerfect(Long idArticle, String articlePerfect) {
        if(articleMapper.updatePerfect(idArticle,articlePerfect) == 0) {
            throw new ContentNotExistException("设置为优选文章失败");
        }
        return true;
    }

    @Override
    public List<ArticleDTO> findAnnouncements() {
        List<ArticleDTO> list = articleMapper.selectAnnouncements();
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
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
