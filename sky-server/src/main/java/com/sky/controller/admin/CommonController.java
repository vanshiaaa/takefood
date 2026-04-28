package com.sky.controller.admin;

import com.sky.config.AliyunOSSOperator;
import com.sky.properties.AliyunOSSProperties;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {

        aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        return null;
    }
}
