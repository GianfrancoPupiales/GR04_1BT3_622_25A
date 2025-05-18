package model.service;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageService {
    private String uploadPath;

    public FileStorageService(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String savePhoto(Part photoPart) throws IOException {
        String photo = null;
        if (photoPart != null && photoPart.getSize() > 0) {
            String fileName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File file = new File(uploadDir, fileName);
            Files.copy(photoPart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            photo = fileName;
        }
        return photo;
    }
}
