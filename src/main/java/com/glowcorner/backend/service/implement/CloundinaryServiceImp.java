package com.glowcorner.backend.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.glowcorner.backend.service.interfaces.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloundinaryServiceImp implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloundinaryServiceImp(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            System.out.println("Cloudinary upload result: " + uploadResult);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new IOException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }
}