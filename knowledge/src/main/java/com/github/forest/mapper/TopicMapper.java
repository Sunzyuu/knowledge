package com.github.forest.mapper;

import com.github.forest.dto.TagDTO;
import com.github.forest.entity.Tag;
import com.github.forest.entity.Topic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 主题表 Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface TopicMapper extends BaseMapper<Topic> {


    /**
     * @param idTopic
     * @return
     */
    List<TagDTO> selectTopicTag(@Param("idTopic") Integer idTopic);

    /**
     * @param idTopic
     * @param tagTitle
     * @return
     */
    List<Tag> selectUnbindTagsById(@Param("idTopic") Long idTopic, @Param("tagTitle") String tagTitle);

}
