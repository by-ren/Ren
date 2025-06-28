package com.ren.cloudstorage.controller;

import com.ren.cloudstorage.properties.AliyunProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ren.cloudstorage.domain.dto.AjaxResultDTO;
import com.ren.cloudstorage.domain.entity.CloudImageLog;
import com.ren.cloudstorage.domain.exception.OSSException;
import com.ren.cloudstorage.properties.CloudStorageProperties;
import com.ren.cloudstorage.service.CloudStorageService;
import com.ren.cloudstorage.service.route.CloudStorageServiceRouter;
import com.ren.cloudstorage.utils.FileUtils;
import com.ren.cloudstorage.utils.StringUtils;

@RestController
@RequestMapping("/cloudStorage")
public class CloudStorageController{

    @Autowired
    private CloudStorageServiceRouter cloudStorageServiceRouter;
    @Autowired
    private AliyunProperties aLiYunProperties;
    @Autowired
    private CloudStorageProperties cloudStorageProperties;

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @param belong 业务归属分类
     * @return 上传后的图片记录
     * @throws OSSException 上传异常
     */
    @PostMapping("/upload")
    public AjaxResultDTO uploadFile(MultipartFile file, String belong) throws OSSException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        //如果没有指定分类，则使用默认分类
        if(StringUtils.isNotBlank(belong)){
            belong = aLiYunProperties.getImageUploadPath() + "/"+ belong;
        }else{
            belong = aLiYunProperties.getImageUploadPath();
        }

        //根据不同的云存储厂商，获取对应的服务
        CloudStorageService cloudStorageService = cloudStorageServiceRouter.getService(cloudStorageProperties.getVendor());
        CloudImageLog cloudImageLog = cloudStorageService.upload(file, belong, true);
        AjaxResultDTO ajax = AjaxResultDTO.success();
        ajax.put("url", cloudImageLog.getImageUrl());
        ajax.put("fileName", cloudImageLog.getName());
        ajax.put("newFileName", FileUtils.getName(cloudImageLog.getName()));
        ajax.put("originalFilename", file.getOriginalFilename());
        return ajax;
    }

    /**
     * 生成文件预览URL接口
     *
     * @param filePath 文件在存储桶中的路径
     * @return 预览URL字符串
     * @throws OSSException 生成异常
     */
    @GetMapping("/preview")
    public String generatePreviewUrl(@RequestParam("filePath") String filePath) throws OSSException {
        //根据不同的云存储厂商，获取对应的服务
        CloudStorageService cloudStorageService = cloudStorageServiceRouter.getService(cloudStorageProperties.getVendor());
        return cloudStorageService.generatePreviewUrl(filePath).toString();
    }

}