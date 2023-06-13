package com.github.forest.lucene.service.impl;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.ArticleTagDTO;
import com.github.forest.dto.Author;
import com.github.forest.entity.User;
import com.github.forest.lucene.lucene.ArticleBeanIndex;
import com.github.forest.lucene.mapper.ArticleLuceneMapper;
import com.github.forest.lucene.model.ArticleLucene;
import com.github.forest.lucene.service.LuceneService;
import com.github.forest.lucene.util.ArticleIndexUtil;
import com.github.forest.lucene.util.LucenePath;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.service.UserService;
import com.github.forest.util.Html2TextUtil;
import com.github.forest.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sunzy
 * @date 2023/6/11 22:38
 */
@Service
@Slf4j
public class LuceneServiceImpl implements LuceneService {

    @Resource
    private ArticleLuceneMapper articleLuceneMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserService userService;



    /**
     * 将文章数据 解析成一个个关键词存储到索引文件中
     * @param list
     */
    @Override
    public void writeArticle(List<ArticleLucene> list) {
        try {
            int totalCount = list.size();
            int perThreadCount = 3000;
            // 加一是为了避免初始化线程池时 参数为0
            int threadCount = totalCount / perThreadCount + (totalCount % perThreadCount == 0 ? 0 : 1);
            ExecutorService pool = Executors.newFixedThreadPool(threadCount);
            CountDownLatch countDownLatch1 = new CountDownLatch(1);
            CountDownLatch countDownLatch2 = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                // 每个线程处理3000条数据
                int start = i * perThreadCount;
                int end = Math.min((i + 1) * perThreadCount, totalCount);
                List<ArticleLucene> dataList = list.subList(start, end);
                // 创建子线程要执行的任务
                Runnable runnable = new ArticleBeanIndex(LucenePath.ARTICLE_INDEX_PATH, i, countDownLatch1, countDownLatch2, dataList);
                // 子线程交给之前创建的线程池执行任务
                pool.execute(runnable);
            }
            countDownLatch1.countDown();
            log.info("开始创建文章内容索引");
            countDownLatch2.await();
            log.info("所有线程都创建索引完毕");
            pool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeArticle(Long id) {
        ArticleIndexUtil.addIndex(articleLuceneMapper.getById(id));
    }

    @Override
    public void writeArticle(ArticleLucene articleLucene) {
        ArticleIndexUtil.addIndex(articleLucene);
    }

    @Override
    public void updateArticle(Long id) {
        ArticleIndexUtil.updateIndex(articleLuceneMapper.getById(id));
    }

    @Override
    public void deleteArticle(Long id) {
        ArticleIndexUtil.deleteIndex(id);
    }

    @Override
    public List<ArticleLucene> searchArticle(String value) {
        return null;
    }

    @Override
    public List<ArticleLucene> getAllArticleLucene() {
        List<ArticleLucene> list = articleLuceneMapper.getAllArticleLucene();
        for (ArticleLucene articleLucene : list) {
            articleLucene.setArticleContent(Html2TextUtil.getContent(articleLucene.getArticleContent()));
        }
        return list;
    }

    @Override
    public List<ArticleDTO> getArticlesByIds(Long[] ids) {
        List<ArticleDTO> list = articleLuceneMapper.getArticlesByIds(ids);
        list.forEach(this::genArticle);
        return list;
    }

    private ArticleDTO genArticle(ArticleDTO articleDTO) {
        Author author = genAuthor(articleDTO);
        articleDTO.setArticleAuthor(author);
        articleDTO.setTimeAgo(Utils.getTimeAgo(articleDTO.getUpdatedTime()));
        List<ArticleTagDTO> tags = articleMapper.selectTags(articleDTO.getIdArticle());
        articleDTO.setTags(tags);
        return articleDTO;
    }

    private Author genAuthor(ArticleDTO articleDTO) {
        Author author = new Author();
        User user = userService.getById(articleDTO.getArticleAuthorId());
        author.setUserNickname(articleDTO.getArticleAuthorName());
        author.setUserAvatarURL(articleDTO.getArticleAuthorAvatarUrl());
        author.setIdUser(articleDTO.getArticleAuthorId());
        author.setUserAccount(user.getAccount());
        return author;
    }
}
