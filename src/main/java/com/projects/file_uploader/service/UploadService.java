package com.projects.file_uploader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UploadService {

    private static final String UPLOAD_DIR = "uploads/";

    public ResponseEntity<String> uploadFile(MultipartFile file) {
        try {

            List<String> supportContentTypes = new ArrayList<>();
            supportContentTypes.add("application/pdf");
            supportContentTypes.add("image/jpeg");
            supportContentTypes.add("image/png");

            if (file.isEmpty()) {
                throw new Exception("Please upload a file");
            } else {

                if (!supportContentTypes.contains(file.getContentType())) {
                    throw new Exception("File type not support");
                }

                File uploadDirectory = new File(UPLOAD_DIR);
                if (!uploadDirectory.exists()) {
                    boolean created = uploadDirectory.mkdirs();
                    if (!created) {
                        throw new Exception("Unable to create directory");
                    }
                    log.info("Upload directory created...");
                }

                // uploads/file-name.jpg
                Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                return ResponseEntity.ok("File uploaded : " + filePath);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}