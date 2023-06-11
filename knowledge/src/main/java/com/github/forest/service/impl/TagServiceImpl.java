package com.github.forest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.forest.core.exception.BusinessException;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.dto.ArticleTagDTO;
import com.github.forest.dto.LabelModel;
import com.github.forest.entity.Article;
import com.github.forest.entity.Tag;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.mapper.TagMapper;
import com.github.forest.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.XssUtils;
import com.github.forest.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

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

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean cleanUnusedTag() {
        return tagMapper.deleteUnusedTag() > 0;
    }

    @Override
    @Transactional(rollbackFor = {UnsupportedEncodingException.class})
    public Integer saveTagArticle(Article article, String articleContentHtml, Long userId) throws UnsupportedEncodingException {
        String articleTags = article.getArticleTags();
        if(StringUtils.isNotBlank(articleTags)) {
            String[] tags = articleTags.split(";");
            List<ArticleTagDTO> articleTagDTOList = articleMapper.selectTags(article.getId());
            for (int i = 0; i < tags.length; i++) {
                boolean addTagArticle = false;
                boolean addTagUser = false;
                Tag tag = query().eq("tag_title", tags[i]).one();
                if(tag == null) {
                    tag = new Tag();

                    tag.setTagTitle(tags[i]);
                    tag.setTagUri(URLEncoder.encode(tag.getTagTitle(), "UTF-8"));
                    tag.setCreatedTime(new Date());
                    tag.setUpdatedTime(tag.getCreatedTime());
                    tag.setTagArticleCount(1);
                    save(tag);
                    addTagArticle = true;
                    addTagUser = true;
                } else {
                    int n = articleTagDTOList.size();
                    for (int m = 0; m < n; m++) {
                        ArticleTagDTO articleTag = articleTagDTOList.get(m);
                        if (articleTag.getIdTag().toString().equals(tag.getId().toString())) {
                            articleTagDTOList.remove(articleTag);
                            m--;
                            n--;
                        }

                        Integer count = tagMapper.selectCountTagArticleById(tag.getId(), article.getId());
                        if (count == 0) {
                            tag.setTagArticleCount(tag.getTagArticleCount() + 1);
                            updateById(tag);
                            addTagArticle = true;
                        }
                    }
                }
                Integer countUserTag = tagMapper.selectCountUserTagById(userId, tag.getId());
                if (countUserTag == 0) {
                    addTagUser = true;
                }
                articleTagDTOList.forEach(articleTagDTO -> {
                    articleMapper.deleteUnusedArticleTag(articleTagDTO.getIdArticleTag());
                });
                if (addTagArticle) {
                    tagMapper.insertTagArticle(tag.getId(), article.getId());
                }
                if (addTagUser) {
                    tagMapper.insertUserTag(tag.getId(), userId, 1);
                }
            }

            return 1;
        } else {
            if(StringUtils.isNotBlank(articleContentHtml)) {
                article.setArticleTags("待分类");
                saveTagArticle(article, articleContentHtml, userId);
            }
        }
        return 0;
    }

    @Override
    public List<LabelModel> findTagLabels() {
        String tags = stringRedisTemplate.opsForValue().get("tags");
        if(StringUtils.isNotBlank(tags)) {
            return JSONObject.parseArray(tags, LabelModel.class);
        } else {
            List<LabelModel> labelModels = tagMapper.selectTagLabels();
            stringRedisTemplate.opsForValue().set("tags", JSONObject.toJSONString(labelModels));
            return labelModels;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag saveTag(Tag tag) {
        boolean result;
        tag.setTagDescription(XssUtils.filterHtmlCode(tag.getTagDescription()));
        if (tag.getId() == null) {
            if(StringUtils.isNotBlank(tag.getTagTitle())) {
                throw new IllegalArgumentException("标签标题不能为空");
            } else {
                Integer count = query().eq("tag_title", tag.getTagTitle()).count();
                if(count > 0) {
                    throw new BusinessException("标签：" + tag.getTagTitle() + "已存在");
                }
            }
            // 处理标签的icon
            if (StringUtils.isNotBlank(tag.getTagIconPath()) && tag.getTagIconPath().contains("base64")) {
                String tagIconPath = UploadController.uploadBase64File(tag.getTagIconPath(), 2);
                tag.setTagIconPath(tagIconPath);
            } else {
                tag.setTagIconPath(tag.getTagIconPath());
            }
            tag.setCreatedTime(new Date());
            tag.setUpdatedTime(tag.getCreatedTime());
            result = save(tag);
        } else {
            tag.setUpdatedTime(new Date());
            if(StringUtils.isNotBlank(tag.getTagIconPath()) && tag.getTagIconPath().contains("base64")){
                String tagIconPath = UploadController.uploadBase64File(tag.getTagIconPath(), 2);
                tag.setTagIconPath(tagIconPath);
            }
            result = updateById(tag);
        }
        if(!result) {
            throw new ServiceException("操作失败！");
        }
        return tag;
    }
}
