package com.mayuan.reggie.controller;


import com.mayuan.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        log.info(file.toString());
        System.out.println("方法被访问了");

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取原始文件的后缀名
        String suffer = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成新的文件名
        String filenName = UUID.randomUUID().toString() + suffer;

        File file1 = new File(basePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        try {
            //指定临时文件转存到指定位置
            file.transferTo(new File(basePath + filenName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(filenName);

    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流来读取文件
            FileInputStream is = new FileInputStream(new File(basePath + name));

            //构造输入流，通过输出流将文件写入浏览器器
            ServletOutputStream outputStream = response.getOutputStream();
            //设置响应的文件类型
            response.setContentType("/image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = (is.read(bytes))) != -1){
                outputStream.write(bytes, 0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
