package com.github.forest.lucene.service.impl;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.lucene.model.ArticleLucene;
import com.github.forest.lucene.service.LuceneService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sunzy
 * @date 2023/6/11 22:38
 */
@Service
public class LuceneServiceImpl implements LuceneService {
    @Override
    public void writeArticle(List<ArticleLucene> list) {

    }

    @Override
    public void writeArticle(Long id) {

    }

    @Override
    public void writeArticle(ArticleLucene articleLucene) {

    }

    @Override
    public void updateArticle(Long id) {

    }

    @Override
    public void deleteArticle(Long id) {

    }

    @Override
    public List<ArticleLucene> searchArticle(String value) {
        return null;
    }

    @Override
    public List<ArticleLucene> getAllArticleLucene() {
        return null;
    }

    @Override
    public List<ArticleDTO> getArticlesByIds(Long[] ids) {
        return null;
    }
}
