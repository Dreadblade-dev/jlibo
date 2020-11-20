package com.dreadblade.jlibo.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    public enum TypeOfFile {
        BOOK_FILE("/books/books"),
        BOOK_IMAGE("/books/images"),
        AUTHOR_IMAGE("/author/images"),
        USER_IMAGE("/user/images");

        private String directory;

        TypeOfFile(String directory) {
            this.directory = directory;
        }

        public String getDirectory() {
            return directory;
        }
    }

    public static String saveFile(MultipartFile file, String uploadPath, TypeOfFile type) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath +type.getDirectory());

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String resultFilename = uuid + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadDir.toString() + "/" + resultFilename));

            return resultFilename;
        }
        throw new RuntimeException("Error when saving file!");
    }
}
