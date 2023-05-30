package com.github.forest.lucene.lucene;

import com.github.forest.lucene.model.UserLucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author sunzy
 * @Date 2023/5/30 17:06
 */
public class UserBeanIndex extends BaseIndex<UserLucene>  {

    public UserBeanIndex(
            String parentIndexPath,
            int subIndex,
            CountDownLatch countDownLatch1,
            CountDownLatch countDownLatch2,
            List<UserLucene> list
            ) {
        super(parentIndexPath, subIndex, countDownLatch1, countDownLatch2, list);
    }


    @Override
    public void indexDoc(IndexWriter writer, UserLucene userLucene) throws Exception {
        Document doc = new Document();
        // 创建对应字段的域
        Field id = new Field("id", userLucene.getIdUser().toString(), TextField.TYPE_STORED);
        Field nickname = new Field("nickname", userLucene.getNickname(), TextField.TYPE_STORED);
        Field signature = new Field("signature", userLucene.getSignature(), TextField.TYPE_STORED);

        // 添加到Document中
        doc.add(id);
        doc.add(nickname);
        doc.add(signature);
        // 如果文件没有创建说明是第一次写入，
        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
            writer.addDocument(doc);
        } else {
            // 根据id字段更新用户信息
            writer.updateDocument(new Term("id", userLucene.getIdUser() + ""), doc);
        }
    }
}
