package com.my.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static String directoryName = System.getProperty("user.dir");
    public static String uPhotoSuffix = "/static/images/";
    public static String uPhotoPrefix = "/Uber/src/main/resources/webapp";
    public static String getPhotoPath(MultipartFile multipartFile){
        String uPhotoPath = uPhotoSuffix + multipartFile.getOriginalFilename();
        File file = new File(directoryName + uPhotoPrefix + uPhotoPath);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uPhotoPath;
    }
}
