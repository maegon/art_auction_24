package com.example.ArtAuction_24.global.base.initData;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileHelper {
    public static MultipartFile createMultipartFile(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream input = new FileInputStream(file);
            return new MockMultipartFile("file", file.getName(), "image/jpeg", input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create MultipartFile from " + filePath, e);
        }
    }
}
