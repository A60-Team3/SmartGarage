package org.example.smartgarage.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.example.smartgarage.exceptions.IllegalFileUploadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTests {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    void uploadImage_Should_Return_ValidUrl_When_ExternalApiWorks() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("file", "Smart Garage Inc".getBytes());
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://example.com/image.jpg");

        Mockito.when(cloudinary.uploader()).thenReturn(uploader);
        Mockito.when(uploader.upload(Mockito.any(), Mockito.anyMap()))
                .thenReturn(uploadResult);

        String result = cloudinaryService.uploadImage(multipartFile);

        Assertions.assertEquals("https://example.com/image.jpg", result);
    }

    @Test
    void uploadImage_Should_Throw_When_ExternalApiRejectsRequest() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("file", "Smart Garage Inc".getBytes());

        Mockito.when(cloudinary.uploader()).thenThrow(IllegalFileUploadException.class);

        Assertions.assertThrows(IllegalFileUploadException.class,
                () -> cloudinaryService.uploadImage(multipartFile));
    }
}
