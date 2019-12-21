package com.sjm.wlkg.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.controller.UploadController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private static final List<String> suffixes = Arrays.asList("image/png","image/jpg", "image/jpeg");

    public String upload(MultipartFile file) {
        try {
            //校验文件类型
            String type = file.getContentType();
            if(!suffixes.contains(type)){
                logger.info("上传失败，文件类型不匹配：{}", type);
                throw  new WlkgException(ExceptionEnums.FILE_TYPE_MISMATCH);
            }

            //校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                logger.info("上传失败，文件内容不符合要求");
                throw  new WlkgException(ExceptionEnums.FILE_TYPE_MISMATCH);
            }

            //保存目录
//            File dir = new File("D:\\upload");
//            if(!dir.exists()){
//                dir.mkdirs();
//            }

            //保存图片
//            file.transferTo(new File(dir,file.getOriginalFilename()));
            String substringAfterLast = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), substringAfterLast, null);

            //拼接图片地址
            String url = "http://image.wlkg.com/" + storePath.getFullPath();
            return url;

        } catch (IOException e) {
            throw  new WlkgException(ExceptionEnums.FILE_ERROR);
        }
    }
}
