package com.csy.summary.daily.minio;

import cn.hutool.core.io.FileTypeUtil;
import com.csy.summary.daily.beans.minio.OssFile;
import com.csy.summary.daily.beans.minio.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author shuyun.cheng
 * @version 1.0
 * @desc 断点续传控制器
 * @date 2022-12-26 17:24
 */
@RestController
@RequestMapping(value = "/file")
@Slf4j
public class MinioResumeController {
    private static final String MD5_KEY = "cn:lyf:minio:demo:file:md5List";

    @Resource
    private MinioResumeUtil minioResumeUtil;

    @Resource
    private MinioProperties minioProperties;

    @Resource(name = "jsonRedisTemplate")
    private RedisTemplate<String, Serializable> jsonRedisTemplate;


    @RequestMapping(value = "/home/upload")
    public ModelAndView homeUpload() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    /**
     * 根据文件大小和文件的md5校验文件是否存在
     * 暂时使用Redis实现，后续需要存入数据库
     * 实现秒传接口
     *
     * @param md5 文件的md5
     * @return 操作是否成功
     */
    @GetMapping(value = "/check")
    public Map<String, Object> checkFileExists(String md5) {
        Map<String, Object> resultMap = new HashMap<>();
        if (ObjectUtils.isEmpty(md5)) {
            resultMap.put("status", StatusCode.PARAM_ERROR.getCode());
            return resultMap;
        }
        // 先从Redis中查询
        String url = (String) jsonRedisTemplate.boundHashOps(MD5_KEY).get(md5);

        // 文件不存在
        if (ObjectUtils.isEmpty(url)) {
            resultMap.put("status", StatusCode.NOT_FOUND.getCode());
            return resultMap;
        }

        resultMap.put("status", StatusCode.SUCCESS.getCode());
        resultMap.put("url", url);
        // 文件已经存在了
        return resultMap;
    }


    /**
     * 文件上传，适合大文件，集成了分片上传
     */
    @PostMapping(value = "/upload")
    public Map<String, Object> upload(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;

        // 获得文件分片数据
        MultipartFile file = multipartRequest.getFile("data");

        // 上传过程中出现异常，状态码设置为50000
        if (file == null) {
            map.put("status", StatusCode.FAILURE.getCode());
            return map;
        }
        // 分片第几片
        int index = Integer.parseInt(multipartRequest.getParameter("index"));
        // 总片数
        int total = Integer.parseInt(multipartRequest.getParameter("total"));
        // 获取文件名
        String fileName = multipartRequest.getParameter("name");

        String md5 = multipartRequest.getParameter("md5");

        // 创建文件桶
        minioResumeUtil.makeBucket(md5);
        String objectName = String.valueOf(index);

        log.info("index: {}, total:{}, fileName:{}, md5:{}, objectName:{}", index, total, fileName, md5, objectName);

        // 当不是最后一片时，上传返回的状态码为20001
        if (index < total) {
            try {
                // 上传文件
                OssFile ossFile = minioResumeUtil.putChunkObject(file.getInputStream(), md5, objectName);
                log.info("{} upload success {}", objectName, ossFile);

                // 设置上传分片的状态
                map.put("status", StatusCode.ALONE_CHUNK_UPLOAD_SUCCESS.getCode());
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", StatusCode.FAILURE.getCode());
                return map;
            }
        } else {
            // 为最后一片时状态码为20002
            try {
                // 上传文件
                minioResumeUtil.putChunkObject(file.getInputStream(), md5, objectName);

                // 设置上传分片的状态
                map.put("status", StatusCode.ALL_CHUNK_UPLOAD_SUCCESS.getCode());
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", StatusCode.FAILURE.getCode());
                return map;
            }
        }

    }

    /**
     * 文件合并
     *
     * @param shardCount 分片总数
     * @param fileName   文件名
     * @param md5        文件的md5
     * @param fileType   文件类型
     * @param fileSize   文件大小
     * @return 分片合并的状态
     */
    @GetMapping(value = "/merge")
    public Map<String, Object> merge(Integer shardCount, String fileName, String md5, String fileType,
                                     Long fileSize) {
        Map<String, Object> retMap = new HashMap<>();

        try {
            // 查询片数据
            List<String> objectNameList = minioResumeUtil.listObjectNames(md5);
            if (shardCount != objectNameList.size()) {
                // 失败
                retMap.put("status", StatusCode.FAILURE.getCode());
            } else {
                // 开始合并请求
                String targetBucketName = minioProperties.getBucket();
                String filenameExtension = StringUtils.getFilenameExtension(fileName);
                String fileNameWithoutExtension = UUID.randomUUID().toString();
                String objectName = fileNameWithoutExtension + "." + filenameExtension;
                OssFile ossFile = minioResumeUtil.composeObject(md5, targetBucketName, objectName);

                log.info("桶：{} 中的分片文件，已经在桶：{},文件 {} 合并成功", md5, targetBucketName, objectName);

                // 合并成功之后删除对应的临时桶
                minioResumeUtil.removeBucket(md5, true);
                log.info("删除桶 {} 成功", md5);

                // 计算文件的md5
                String fileMd5 = null;
                try (InputStream inputStream = minioResumeUtil.getObject(targetBucketName, objectName)) {
                    fileMd5 = Md5Util.calculateMd5(inputStream);
                } catch (IOException e) {
                    log.error("", e);
                }

                // 计算文件真实的类型
                String type = null;
                try (InputStream inputStreamCopy = minioResumeUtil.getObject(targetBucketName, objectName)) {
                    type = FileTypeUtil.getType(inputStreamCopy);
                } catch (IOException e) {
                    log.error("", e);
                }

                // 并和前台的md5进行对比
                if (!ObjectUtils.isEmpty(fileMd5) && !ObjectUtils.isEmpty(type) && fileMd5.equalsIgnoreCase(md5) && type.equalsIgnoreCase(fileType)) {
                    // 表示是同一个文件, 且文件后缀名没有被修改过
                    String url = minioResumeUtil.getPreSignedObjectUrl(targetBucketName, objectName);

                    // 存入redis中
                    jsonRedisTemplate.boundHashOps(MD5_KEY).put(fileMd5, url);

                    // 成功
                    retMap.put("status", StatusCode.SUCCESS.getCode());
                } else {
                    log.error("非法的文件信息: 分片数量:{}, 文件名称:{}, 文件md5:{}, 文件类型:{}, 文件大小:{}",
                            shardCount, fileName, md5, fileType, fileSize);

                    // 并需要删除对象
                    minioResumeUtil.deleteObject(targetBucketName, objectName);
                    retMap.put("status", StatusCode.FAILURE.getCode());
                }
            }
        } catch (Exception e) {
            log.error("", e);
            // 失败
            retMap.put("status", StatusCode.FAILURE.getCode());
        }
        return retMap;
    }

}
