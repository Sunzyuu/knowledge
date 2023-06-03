package com.github.forest.web.api.common;

import org.apache.commons.codec.binary.Base64;
import com.alibaba.fastjson.JSONObject;
import com.github.forest.auth.JwtConstants;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.dto.TokenUser;
import com.github.forest.service.FileService;
import com.github.forest.util.FileUtils;
import com.github.forest.util.SpringContextHolder;
import com.github.forest.util.UserUtils;
import com.github.forest.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

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


    public static String uploadBase64File(String fileStr, Integer type) {
        if(StringUtils.isBlank(fileStr)) {
            return  "";
        }
        String typePath = getTypePath(type);
        //图片存储路径
        String ctxHeadPicPath = env.getProperty("resource.pic-path");
        String dir = ctxHeadPicPath + "/" + typePath;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }


        String localPath = Utils.getProperty("resource.file-path") + "/" + typePath + "/";
        String fileName = System.currentTimeMillis() + ".png";
        String savePath = file.getPath() + File.separator + fileName;
        File saveFile = new File(savePath);
        try {
            FileCopyUtils.copy(Base64.decodeBase64(fileStr.substring(fileStr.indexOf(",") + 1)), saveFile);
            fileStr = localPath + fileName;
        } catch (IOException e) {
            fileStr = "上传失败!";
        }
        return fileStr;
    }


    @PostMapping("/file")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResult<JSONObject> uploadPicture(@RequestParam(value = "file", required = false) MultipartFile multipartFile, @RequestParam(defaultValue = "1") Integer type, HttpServletRequest request) throws IOException {
        if(multipartFile == null) {
            return GlobalResultGenerator.genErrorResult("请选择要上传的文件！");
        }
        TokenUser tokenUser = getTokenUser(request);
//        TokenUser tokenUser = new TokenUser(); // 测试使用
//        tokenUser.setIdUser(8l);
        JSONObject data = new JSONObject(2);
        if(multipartFile.getSize() == 0){
            data.put("message", "上传失败！");
            return GlobalResultGenerator.genSuccessResult(data);
        }
        String md5 = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = FileUtils.getExtend(originalFilename);
        String fileUrl = fileService.getFileUrlByMd5(md5, tokenUser.getIdUser(), fileType);
        if(StringUtils.isNotEmpty(fileUrl)) {
            // 说明是重复的文件，直接返回url
            data.put("url", fileUrl);
            return GlobalResultGenerator.genSuccessResult(data);
        }
        String typePath = getTypePath(type);
        // 图片的存储路径
        String ctxHeadPicPath = env.getProperty("resource.pic-path");
        String dir = ctxHeadPicPath + "/" + typePath;
        File file = new File(dir);
        if(!file.exists()) {
            // 创建图片保存的根目录
            file.mkdirs();
        }

        // 本地保存路径
        String localPath = Utils.getProperty("resource.file-path")+ "/" + typePath + "/";
        String fileName = System.currentTimeMillis() + fileType;
        String savePath = file.getPath() + File.separator + fileName;
        fileUrl = localPath + fileName;
        File saveFile = new File(savePath);
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
            fileService.insertForestFile(fileUrl, savePath, md5, tokenUser.getIdUser(), multipartFile.getSize(), fileType);
            data.put("url", fileUrl);
            data.put("message", "上传成功！");
        } catch (IOException e) {
            data.put("message", "上传失败！");
        }

        return GlobalResultGenerator.genSuccessResult(data);
    }

    private TokenUser getTokenUser(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtConstants.UPLOAD_TOKEN);
        if(StringUtils.isBlank(authHeader)) {
            throw new UnauthenticatedException();
        }
        return UserUtils.getTokenUser(authHeader);
    }


}
