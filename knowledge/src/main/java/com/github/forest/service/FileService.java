package com.github.forest.service;

import com.github.forest.entity.ForestFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文件上传记录表 服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-02
 */
public interface FileService extends IService<ForestFile> {

    /**
     * 通过md5获取文件访问链接
     *
     * @param md5Value  md5值
     * @param createdBy 用户id
     * @param fileType  文件类型
     * @return
     */
    String getFileUrlByMd5(String md5Value, long createdBy, String fileType);


    /**
     * 插入文件对象
     * @param fileUrl   访问路径
     * @param filePath  上传路径
     * @param md5Value  md5值
     * @param createdBy 创建人
     * @param fileSize  文件大小
     * @param fileType  文件类型
     * @return
     */
    int insertForestFile(String fileUrl, String filePath, String md5Value, long createdBy, long fileSize, String fileType);

}
