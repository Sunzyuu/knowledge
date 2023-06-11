package com.github.forest.mapper;

import com.github.forest.dto.LabelModel;
import com.github.forest.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 插入标签文章表(forest_tag_article)相关信息
     *
     * @param idTag
     * @param idArticle
     * @return
     */
    Integer insertTagArticle(@Param("idTag") Long idTag, @Param("idArticle") Long idArticle);

    /**
     * 统计标签使用数(文章)
     *
     * @param idTag
     * @param idArticle
     * @return
     */
    Integer selectCountTagArticleById(@Param("idTag") Long idTag, @Param("idArticle") Long idArticle);

    /**
     * 获取用户标签数
     *
     * @param idUser
     * @param idTag
     * @return
     */
    Integer selectCountUserTagById(@Param("idUser") Long idUser, @Param("idTag") Long idTag);

    /**
     * 插入用户标签信息
     *
     * @param idTag
     * @param idUser
     * @param type
     * @return
     */
    Integer insertUserTag(@Param("idTag") Long idTag, @Param("idUser") Long idUser, @Param("type") Integer type);

    /**
     * 删除未使用标签
     *
     * @return
     */
    Integer deleteUnusedTag();

    /**
     * 查询标签列表
     *
     * @return
     */
    List<LabelModel> selectTagLabels();
}
