package com.github.forest.lucene;

import com.github.forest.entity.User;
import com.github.forest.service.UserService;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author sunzy
 * @Date 2023/5/30 15:52
 */
@SpringBootTest
public class demo {

    @Resource
    private UserService userService;

    @Test
    void createIndex() throws IOException {
        // 采集数据 创建几个数据测试一下
        List<User> list = userService.list();
        List<Document> documents = new ArrayList<>();

        for (User user : list) {
            Document document = new Document();
            document.add(new TextField("id", user.getId().toString(), Field.Store.YES));
            document.add(new TextField("nickname", user.getNickname(), Field.Store.YES));
            document.add(new TextField("account", user.getAccount(), Field.Store.YES));
            document.add(new TextField("email", user.getEmail(), Field.Store.YES));
            document.add(new TextField("signature", user.getSignature(), Field.Store.YES));
            documents.add(document);
        }

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory dir;
        try {
            dir = FSDirectory.open(Paths.get("E:\\Sunzh\\java\\maven\\knowledge\\knowledge\\src\\main\\resources\\lucene\\dir"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, writerConfig);

        // 写入文档库
        for (Document document : documents) {
            writer.addDocument(document);
        }
        // 释放资源
        writer.close();

    }

    @Test
    void testSearch() throws ParseException, IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("nickname", analyzer);
        Query query = queryParser.parse("jack");

        FSDirectory open = FSDirectory.open(Paths.get("E:\\Sunzh\\java\\maven\\knowledge\\knowledge\\src\\main\\resources\\lucene\\dir"));
        DirectoryReader directoryReader = DirectoryReader.open(open);

        IndexSearcher searcher = new IndexSearcher(directoryReader);
        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(scoreDoc.toString());
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);

            System.out.println("==============");
        }
        directoryReader.close();
    }
}
