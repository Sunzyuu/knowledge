package com.github.forest.web.api.common;

import com.github.forest.dto.LinkToImageUrlDTO;
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
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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

    @PostMapping("/file/batch")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResult<JSONObject> batchFileUpload(@RequestParam(value = "file[]", required = false) MultipartFile[] multipartFiles, @RequestParam(defaultValue = "1") Integer type, HttpServletRequest request) throws IOException {

        if(multipartFiles == null) {
            return GlobalResultGenerator.genErrorResult("请选择要上传的文件！");
        }
        TokenUser tokenUser = getTokenUser(request);
//        TokenUser tokenUser = new TokenUser(); // 测试使用
//        tokenUser.setIdUser(8l);
        JSONObject data = new JSONObject(2);
        String typePath = getTypePath(type);
        // 图片的存储路径
        String ctxHeadPicPath = env.getProperty("resource.pic-path");
        String dir = ctxHeadPicPath + "/" + typePath;
        File file = new File(dir);
        if(!file.exists()) {
            // 创建图片保存的根目录
            file.mkdirs();
        }
        String localPath = Utils.getProperty("resource.file-path")+ "/" + typePath + "/";
        Map successMap = new HashMap(16);
        Set errFiles = new HashSet();
        for (int i = 0; i < multipartFiles.length; i++) {
            MultipartFile multipartFile = multipartFiles[i];
            String md5 = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
            String originalFilename = multipartFile.getOriginalFilename();
            String fileType = FileUtils.getExtend(originalFilename);
            String fileUrl = fileService.getFileUrlByMd5(md5, tokenUser.getIdUser(), fileType);
            if(StringUtils.isNotEmpty(fileUrl)) {
                // 说明是重复的文件，直接返回url
                successMap.put(originalFilename, fileUrl);
                continue;
            }

            // 本地保存路径
            String fileName = System.currentTimeMillis() + fileType;
            String savePath = file.getPath() + File.separator + fileName;
            fileUrl = localPath + fileName;
            File saveFile = new File(savePath);
            try {
                FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
                fileService.insertForestFile(fileUrl, savePath, md5, tokenUser.getIdUser(), multipartFile.getSize(), fileType);
                successMap.put(originalFilename, localPath + fileName);
            } catch (IOException e) {
                errFiles.add(originalFilename);
            }
        }
        data.put("errFiles", errFiles);
        data.put("successMap", successMap);

        return GlobalResultGenerator.genSuccessResult(data);
    }

    private TokenUser getTokenUser(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtConstants.UPLOAD_TOKEN);
        if(StringUtils.isBlank(authHeader)) {
            throw new UnauthenticatedException();
        }
        return UserUtils.getTokenUser(authHeader);
    }

    @GetMapping("/simple/token")
    public GlobalResult<com.alibaba.fastjson2.JSONObject> uploadSimpleToken(HttpServletRequest request) {
        return getUploadToken(request, UPLOAD_SIMPLE_URL);
    }

    @GetMapping("/token")
    public GlobalResult<com.alibaba.fastjson2.JSONObject> uploadToken(HttpServletRequest request) {
        return getUploadToken(request, UPLOAD_URL);
    }

    private GlobalResult<com.alibaba.fastjson2.JSONObject> getUploadToken(HttpServletRequest request, String uploadUrl) {
        String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)) {
            throw new UnauthorizedException();
        }
        TokenUser tokenUser = UserUtils.getTokenUser(authHeader);
        com.alibaba.fastjson2.JSONObject jsonObject = new com.alibaba.fastjson2.JSONObject();
        jsonObject.put("uploadToken", tokenUser.getToken());
        jsonObject.put("uploadURL", uploadUrl);
        jsonObject.put("linkToImageURL", LINK_TO_IMAGE_URL);
        return GlobalResultGenerator.genSuccessResult(jsonObject);
    }


    @PostMapping("/file/link")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResult linkToImageUrl(@RequestBody LinkToImageUrlDTO linkToImageUrlDTO, HttpServletRequest request) throws IOException {
        TokenUser tokenUser = getTokenUser(request);
        String url = linkToImageUrlDTO.getUrl();
        HashMap<Object, Object> data = new HashMap<>(2);
        if (StringUtils.isBlank(url)) {
            data.put("message", "文件为空!");
            return GlobalResultGenerator.genSuccessResult(data);
        }
        if (url.contains(Utils.getProperty("resource.file-path"))) {
            data.put("originalURL", url);
            data.put("url", url);
            return GlobalResultGenerator.genSuccessResult(data);
        }
        URL link = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) link.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");
        conn.setRequestProperty("referer", "");

        // 得到输入流
        try (InputStream inputStream = conn.getInputStream()) {
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            if (getData.length == 0) {
                data.put("message", "文件为空!");
                return GlobalResultGenerator.genSuccessResult(data);
            }
            // 获取文件md5值
            String md5 = DigestUtils.md5DigestAsHex(getData);
            String fileType = "." + MimeTypeUtils.parseMimeType(conn.getContentType()).getSubtype();
            String fileUrl = fileService.getFileUrlByMd5(md5, tokenUser.getIdUser(), fileType);

            data.put("originalURL", url);

            if (StringUtils.isNotEmpty(fileUrl)) {
                data.put("url", fileUrl);
                return GlobalResultGenerator.genSuccessResult(data);
            }

            Integer type = linkToImageUrlDTO.getType();
            if (Objects.isNull(type)) {
                type = 1;
            }
            String typePath = getTypePath(type);
            //图片存储路径
            String ctxHeadPicPath = env.getProperty("resource.pic-path");
            String dir = ctxHeadPicPath + "/" + typePath;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String fileName = System.currentTimeMillis() + fileType;
            fileUrl = Utils.getProperty("resource.file-path") + "/" + typePath + "/" + fileName;

            String savePath = file.getPath() + File.separator + fileName;
            File saveFile = new File(savePath);

            FileCopyUtils.copy(getData, saveFile);
            fileService.insertForestFile(fileUrl, savePath, md5, tokenUser.getIdUser(), getData.length, fileType);
            data.put("originalURL", url);
            data.put("url", fileUrl);
            return GlobalResultGenerator.genSuccessResult(data);
        } catch (IOException e) {
            log.error("link: {}, \nmessage: {}", url, e.getMessage());
            data.put("originalURL", url);
            data.put("url", url);
            return GlobalResultGenerator.genSuccessResult(data);
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
