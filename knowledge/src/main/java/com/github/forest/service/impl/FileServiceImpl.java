package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.forest.entity.ForestFile;
import com.github.forest.mapper.FileMapper;
import com.github.forest.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 文件上传记录表 服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-02
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, ForestFile> implements FileService {

    @Override
    public String getFileUrlByMd5(String md5Value, long createdBy, String fileType) {
        LambdaQueryWrapper<ForestFile> fileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        fileLambdaQueryWrapper.eq(true, ForestFile::getMd5Value, md5Value);
        fileLambdaQueryWrapper.eq(true, ForestFile::getCreatedBy, createdBy);
        fileLambdaQueryWrapper.eq(true, ForestFile::getFileType, fileType);
        ForestFile file = getOne(fileLambdaQueryWrapper);
        if(file == null) {
            return "";
        }
        return file.getFileUrl();
    }

    @Override
    public int insertForestFile(String fileUrl, String filePath, String md5Value, long createdBy, long fileSize, String fileType) {
        ForestFile file = new ForestFile();
        file.setFileUrl(fileUrl);
        file.setFilePath(filePath);
        file.setMd5Value(md5Value);
        file.setCreatedBy(createdBy);
        file.setFileSize(fileSize);
        file.setFileType(fileType);
        file.setCreatedTime(new Date());
        file.setUpdatedTime(new Date());
        if (!save(file)) {
            return 0;
        }
        return 1;
    }
}
