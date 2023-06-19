package com.github.forest.web.api.admin;

import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.entity.Article;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.service.ArticleService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sunzy
 * @date 2023/6/19 22:40
 */
@RestController
@RequestMapping("/api/v1/admin/article")
public class AdminArticleController {


    @Resource
    private ArticleService articleService;

    @PatchMapping("/update-perfect")
    public GlobalResult<Boolean> updatePerfect(@RequestBody Article article) {
        Long idArticle = article.getId();
        String articlePerfect = article.getArticlePerfect();
        return GlobalResultGenerator.genSuccessResult(articleService.updatePerfect(idArticle, articlePerfect));

    }
}
