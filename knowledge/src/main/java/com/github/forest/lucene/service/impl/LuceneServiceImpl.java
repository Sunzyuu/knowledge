package com.github.forest.lucene.service.impl;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.ArticleTagDTO;
import com.github.forest.dto.Author;
import com.github.forest.entity.User;
import com.github.forest.lucene.lucene.ArticleBeanIndex;
import com.github.forest.lucene.lucene.IKAnalyzer;
import com.github.forest.lucene.mapper.ArticleLuceneMapper;
import com.github.forest.lucene.model.ArticleLucene;
import com.github.forest.lucene.service.LuceneService;
import com.github.forest.lucene.util.ArticleIndexUtil;
import com.github.forest.lucene.util.LucenePath;
import com.github.forest.lucene.util.SearchUtil;
import com.github.forest.mapper.ArticleMapper;
import com.github.forest.service.UserService;
import com.github.forest.util.Html2TextUtil;
import com.github.forest.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        List<ArticleLucene> resList = new ArrayList<>();
        ExecutorService service = Executors.newCachedThreadPool();
        // 定义分词器
        Analyzer analyzer = new IKAnalyzer();
        try {
            IndexSearcher searcher =
                    SearchUtil.getIndexSearcherByParentPath(LucenePath.ARTICLE_INDEX_PATH, service);
            String[] fields = {"title", "summary"};

            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            String line = value != null ? value : in.readLine();
            Query query = parser.parse(line);
            SimpleHTMLFormatter htmlFormatter =
                    new SimpleHTMLFormatter("<font color=" + "\"" + "red" + "\"" + ">", "</font>");
            // 高亮搜索的词添加到高亮处理器中
            Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
            // 获取搜索的结果，指定返回document返回的个数
            // TODO 默认搜索结果为显示第一页，1000 条，可以优化
            TopDocs results = SearchUtil.getScoreDocsByPerPage(1, 100, searcher, query);
            ScoreDoc[] hits = results.scoreDocs;
            // 遍历，输出
            for (ScoreDoc hit : hits) {
                int id = hit.doc;
                float score = hit.score;
                Document hitDoc = searcher.doc(hit.doc);
                // 获取到summary
                String summary = hitDoc.get("summary");
                // 将查询的词和搜索词匹配，匹配到添加前缀和后缀
                TokenStream tokenStream = TokenSources.getTokenStream("summary", searcher.getIndexReader().getTermVectors(id), summary, analyzer, -1);
                // 传入的第二个参数是查询的值
                TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, summary, false, 10);
                StringBuilder baikeValue = new StringBuilder();
                for (TextFragment textFragment : frag) {
                    if ((textFragment != null) && (textFragment.getScore() > 0)) {
                        //  if ((frag[j] != null)) {
                        // 获取 summary 的值
                        baikeValue.append(textFragment);
                    }
                }

                // 获取到title
                String title = hitDoc.get("title");
                TokenStream titleTokenStream = TokenSources.getTokenStream("title", searcher.getIndexReader().getTermVectors(id), title, analyzer, -1);
                TextFragment[] titleFrag =
                        highlighter.getBestTextFragments(titleTokenStream, title, false, 10);
                StringBuilder titleValue = new StringBuilder();
                for (int j = 0; j < titleFrag.length; j++) {
                    if ((frag[j] != null)) {
                        titleValue.append(titleFrag[j].toString());
                    }
                }
                resList.add(
                        ArticleLucene.builder()
                                .idArticle(Long.valueOf(hitDoc.get("id")))
                                .articleTitle(titleValue.toString())
                                .articleContent(baikeValue.toString())
                                .score(String.valueOf(score))
                                .build());
            }

        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
        return resList;
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
