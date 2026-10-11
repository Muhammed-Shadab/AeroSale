package com.miniProject.AeroScale.product.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService {


    private final Cloudinary cloudinary;
    public String saveImage(MultipartFile file) {
        try{
            Map map = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return map.get("secure_url").toString();
        } catch (IOException e) {
            log.error("Io Exception Occured During saving the image");
            throw new RuntimeException(e);
        }
    }
}
