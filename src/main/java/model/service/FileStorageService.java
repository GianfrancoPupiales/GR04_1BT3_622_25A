package model.service;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageService {
    private final String uploadPath;

    public FileStorageService(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String savePhoto(Part photoPart) throws IOException {
        String photo = null;
        if (photoPart != null && photoPart.getSize() > 0) {
            String originalFileName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File file = new File(uploadDir, uniqueFileName);
            Files.copy(photoPart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            photo = uniqueFileName;
        }
        return photo;
    }
}