package org.example.smartgarage.services;

import com.cloudinary.Cloudinary;
import org.example.smartgarage.exceptions.IllegalFileUploadException;
import org.example.smartgarage.services.contracts.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) {
        try {
            return this.cloudinary
                    .uploader()
                    .upload(multipartFile.getBytes(), Collections.emptyMap())
                    .get("secure_url")
                    .toString();
        } catch (Exception e) {
            throw new IllegalFileUploadException("Failed to upload file");
        }
    }
}
