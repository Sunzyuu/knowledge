package com.github.forest.service.impl;

import com.github.forest.entity.Comment;
import com.github.forest.mapper.CommentMapper;
import com.github.forest.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
