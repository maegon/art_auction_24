package com.example.ArtAuction_24.global;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    public static MultipartFile convertToMultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        String fileName = file.getName();
        String contentType = "image/jpeg"; // 파일 타입을 적절히 설정
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(fileName, fileName, contentType, input);
    }
}