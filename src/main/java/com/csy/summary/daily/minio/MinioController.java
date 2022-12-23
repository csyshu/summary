package com.csy.summary.daily.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuyun.cheng
 */
@Slf4j
@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private MinioClient minioClient;

    @PostMapping("/upload")
    public ResultBean upload(@RequestParam(name = "file", required = false) MultipartFile[] file) {
        if (file == null || file.length == 0) {
            return ResultBean.error("上传文件不能为空");
        }
        List<String> orgfileNameList = new ArrayList<>(file.length);
        for (MultipartFile multipartFile : file) {
            String originalFilename = multipartFile.getOriginalFilename();
            orgfileNameList.add(originalFilename);
            try {
                //文件上传
                InputStream in = multipartFile.getInputStream();
                minioClient.putObject(PutObjectArgs.builder().bucket(minioProperties.getBucket()).object(originalFilename)
                        .stream(in, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
                in.close();
            } catch (Exception e) {
                log.error(e.getMessage());
                return ResultBean.error("上传失败");
            }
        }
        Map<String, Object> data = new HashMap<>(2);
        data.put("bucketName", minioProperties.getBucket());
        data.put("fileName", orgfileNameList);
        return ResultBean.ok("上传成功", data);
    }

    @PostMapping(value = "/uploadAndGet")
    public String upload(@RequestParam(name = "file") MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        MinioUtil.createBucket(minioProperties.getBucket());
        MinioUtil.uploadFile(minioProperties.getBucket(), multipartFile, fileName);
        return MinioUtil.getPreSignedObjectUrl(minioProperties.getBucket(), fileName);
    }

    @GetMapping("/getInfo")
    public String getInfo(String fileName) throws Exception {
        return MinioUtil.getFileStatusInfo(minioProperties.getBucket(), fileName);
    }

    @GetMapping("/list")
    public List<Object> list(String prefix) throws Exception {
        //获取bucket列表
        return MinioUtil.getFolderList(minioProperties.getBucket(), prefix, false);
    }

    @RequestMapping("/download/{fileName}")
    public void download(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        InputStream in = null;
        try {
            // 获取对象信息
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(minioProperties.getBucket()).object(fileName).build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //文件下载
            in = minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getBucket()).object(fileName).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResultBean delete(@PathVariable("fileName") String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioProperties.getBucket()).object(fileName).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultBean.error("删除失败");
        }
        return ResultBean.ok("删除成功", null);
    }
}