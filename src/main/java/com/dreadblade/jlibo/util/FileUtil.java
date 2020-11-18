package com.dreadblade.jlibo.util;

import com.dreadblade.jlibo.domain.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    public static void saveBookFiles(MultipartFile file, Book book, String uploadPath) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String fileDirectory = file.getOriginalFilename().endsWith(".pdf") ? "/books" : "/images";
            File uploadDir = new File(uploadPath + fileDirectory);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String resultFilename = uuid + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadDir.toString() + "/" + resultFilename));

            if (file.getOriginalFilename().endsWith(".pdf")) {
                book.setBookFilename(resultFilename);
            } else {
                book.setImageFilename(resultFilename);
            }
        }
    }
}
