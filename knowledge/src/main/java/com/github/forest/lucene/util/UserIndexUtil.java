package com.github.forest.lucene.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.forest.lucene.model.ArticleLucene;
import com.github.forest.lucene.model.UserLucene;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.forest.lucene.util.LucenePath.INDEX_PATH;

/**
 * @Author sunzy
 * @Date 2023/5/30 17:31
 */
@Slf4j
public class UserIndexUtil {
    /**
     * lucene索引保存目录
     */
    private static final String PATH = System.getProperty("user.dir") + StrUtil.SLASH + LucenePath.ARTICLE_INDEX_PATH;


    public static void deleteAllIndex(){
        if(FileUtil.exist(LucenePath.ARTICLE_INCREMENT_INDEX_PATH)) {
            FileUtil.del(LucenePath.ARTICLE_INCREMENT_INDEX_PATH);
        }
    }

    public static void addIndex(UserLucene t){
        createIndex(t);
    }

    public static void updateIndex(UserLucene t) {
        deleteIndex(t.getIdUser());
        createIndex(t);
    }

    private static void createIndex(UserLucene t) {
        log.info("创建单个索引");
        IndexWriter writer;
        try {
            boolean create = true;
            if (FileUtil.exist(LucenePath.USER_INCREMENT_INDEX_PATH)) {
                create = false;
            }
            writer = IndexUtil.getIndexWriter(INDEX_PATH, create);
            Document doc = new Document();
            doc.add(new StringField("id", t.getIdUser() + "", Field.Store.YES));
            doc.add(new TextField("nickname", t.getNickname(), Field.Store.YES));
            // 新注册用户无签名
            doc.add(new TextField("signature", StringUtils.isNotBlank(t.getSignature()) ? t.getSignature() : "", Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void deleteIndex(Long id) {
        Arrays.stream(FileUtil.ls(PATH)).forEach(
                each -> {
                    IndexWriter writer;
                    ReentrantLock reentrantLock = new ReentrantLock();
                    reentrantLock.lock();
                    try {
                        writer = IndexUtil.getIndexWriter(each.getAbsolutePath(), false);
                        writer.deleteDocuments(new Term("id", String.valueOf(id)));
                        // 强制删除
                        writer.forceMerge(1);
                        writer.forceMergeDeletes();
                        writer.commit();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        reentrantLock.unlock();
                    }
                }
        );
    }


}
