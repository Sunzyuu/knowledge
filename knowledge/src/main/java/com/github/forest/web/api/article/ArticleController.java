package com.github.forest.web.api.article;


import com.github.forest.core.exception.BusinessException;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.core.service.security.AuthorshipInterceptor;
import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.CommentDTO;
import com.github.forest.entity.Article;
import com.github.forest.entity.ArticleThumbsUp;
import com.github.forest.entity.User;
import com.github.forest.enumerate.Module;
import com.github.forest.service.ArticleService;
import com.github.forest.service.ArticleThumbsUpService;
import com.github.forest.service.CommentService;
import com.github.forest.util.UserUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文章表  前端控制器
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleThumbsUpService articleThumbsUpService;

    @GetMapping("/detail/{idArticle}")
    public GlobalResult<ArticleDTO> detail(@PathVariable Long idArticle, @RequestParam(defaultValue = "2") Integer type) {
        ArticleDTO dto = articleService.findArticleDTOById(idArticle, type);
        return GlobalResultGenerator.genSuccessResult(dto);
    }

    @PostMapping("/post")
    public GlobalResult<Long> postArticle(@RequestBody ArticleDTO article) throws UnsupportedEncodingException {
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.postArticle(article, user));
    }

    @PutMapping("/post")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult<Long> updateArticle(@RequestBody ArticleDTO article) throws UnsupportedEncodingException {
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.postArticle(article, user));
    }

    @GetMapping("/{idArticle}/comments")
    public GlobalResult<PageInfo<CommentDTO>> commons(@PathVariable Integer idArticle, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<CommentDTO> list = commentService.getArticleComments(idArticle);
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/drafts")
    public GlobalResult<PageInfo<ArticleDTO>> drafts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        User user = UserUtils.getCurrentUserByToken();
        List<ArticleDTO> list = articleService.findDrafts(user.getId());
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/{idArticle}/share")
    public GlobalResult<String> share(@PathVariable Integer idArticle) {
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genResult(true, articleService.share(idArticle, user.getAccount()), "");
    }

    @PostMapping("/update-tags")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE_TAG)
    public GlobalResult<Boolean> updateTags(@RequestBody Article article) throws UnsupportedEncodingException {
        Long idArticle = article.getId();
        String articleTags = article.getArticleTags();
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.updateTags(idArticle, articleTags, user.getId()));
    }

    @PostMapping("/thumbs-up")
    public GlobalResult<Integer> thumbsUp(@RequestBody ArticleThumbsUp articleThumbsUp) {
        if (Objects.isNull(articleThumbsUp) || Objects.isNull(articleThumbsUp.getIdArticle())) {
            throw new BusinessException("数据异常,文章不存在!");
        }
        User user = UserUtils.getCurrentUserByToken();
        articleThumbsUp.setIdUser(user.getId());
        return GlobalResultGenerator.genSuccessResult(articleThumbsUpService.thumbsUp(articleThumbsUp));
    }
}
