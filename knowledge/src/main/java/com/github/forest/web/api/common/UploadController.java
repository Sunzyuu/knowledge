package com.github.forest.web.api.common;

import com.github.forest.service.FileService;
import com.github.forest.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sunzy
 * @date 2023/6/2 15:08
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    private static final String UPLOAD_SIMPLE_URL = "/api/upload/file";
    private final static String UPLOAD_URL = "/api/upload/file/batch";
    private final static String LINK_TO_IMAGE_URL = "/api/upload/file/link";
    private static final Environment env = SpringContextHolder.getBean(Environment.class);

    @Resource
    private FileService fileService;

    /**
     * 获取文件保存的路径
     * @param type
     * @return
     */
    private static String getTypePath(Integer type) {
        String typePath;
        switch (type) {
            case 0:
                typePath = "avatar";
                break;
            case 1:
                typePath = "article";
                break;
            case 2:
                typePath = "tag";
                break;
            case 3:
                typePath = "topic";
                break;
            default:
                typePath = "images";
        }
        return typePath;
    }


}
