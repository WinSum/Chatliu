package com.winsum.chatliu.controller;


import com.winsum.chatliu.dto.FileDTO;
import com.winsum.chatliu.provider.TencentCOS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    private TencentCOS tencentCOS;

    @RequestMapping("file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {
        FileDTO fileDTO = new FileDTO();
        MultipartHttpServletRequest mutipartrequest = (MultipartHttpServletRequest) request;;
        MultipartFile multipartFile = mutipartrequest.getFile("editormd-image-file");

        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
        if (fileName.lastIndexOf(".") < 0)
        {
            fileDTO.setSuccess(-1);
            fileDTO.setMessage("上传失败");
            fileDTO.setUrl(null);
            return fileDTO;
        }

        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));


        //使用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);
        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageNameUrl = TencentCOS.uploadfile(excelFile, "chatliu_images");

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        fileDTO.setSuccess(1);
        fileDTO.setMessage("上传成功");
        fileDTO.setUrl(imageNameUrl);
        //返回成功信息
        return fileDTO;

    }

    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
