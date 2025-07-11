package com.example.rentalcarsystem.untils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
public class FileStoreService {
    public String saveFile(MultipartFile file, String rootPath, String saveLocation) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fullDir = rootPath + File.separator + saveLocation;
            File dir = new File(fullDir);
            if (!dir.exists()) dir.mkdirs(); // tạo thư mục nếu chưa tồn tại
            Path path = Paths.get(fullDir + File.separator + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return saveLocation + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }

    }
}
