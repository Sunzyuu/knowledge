package com.github.forest.service.impl;

import com.github.forest.core.exception.BusinessException;
import com.github.forest.core.exception.ServiceException;
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

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;

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
    public Integer saveTagArticle(Article article, String articleContentHtml, Long userId) throws UnsupportedEncodingException {
        return null;
    }

    @Override
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
