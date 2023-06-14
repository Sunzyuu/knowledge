package com.github.forest.lucene.service.impl;

import com.github.forest.lucene.dic.Dictionary;
import com.github.forest.lucene.mapper.UserDicMapper;
import com.github.forest.lucene.model.UserDic;
import com.github.forest.lucene.service.UserDicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author sunzy
 * @date 2023/6/14 19:39
 */
@Service
public class UserDicServiceImpl implements UserDicService {

    @Resource
    private UserDicMapper userDicMapper;

    @Override
    public List<String> getAllDic() {

        return userDicMapper.getAllDic();
    }

    @Override
    public List<UserDic> getAll() {
        return userDicMapper.getAll();
    }

    @Override
    public void addDic(String dic) {
        userDicMapper.addDic(dic);
        writeUserDic();
    }

    @Override
    public void deleteDic(String id) {
        userDicMapper.deleteDic(id);
        writeUserDic();
    }

    @Override
    public void updateDic(UserDic userDic) {
        userDicMapper.updateDic(userDic.getId(), userDic.getDic());
        writeUserDic();
    }

    @Override
    public void writeUserDic() {
        try {
            String filePath = "lucene/userDic/";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream stream = new FileOutputStream(file + "/userDic.dic", false);
            OutputStreamWriter outfw = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
            PrintWriter fw = new PrintWriter(new BufferedWriter(outfw));
            userDicMapper
                    .getAllDic()
                    .forEach(
                            each -> {
                                fw.write(each);
                                fw.write("\r\n");
                            });
            fw.flush();
            fw.close();
            Dictionary.getSingleton().updateUserDict();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}