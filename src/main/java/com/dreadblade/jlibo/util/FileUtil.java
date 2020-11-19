package com.dreadblade.jlibo.util;

import com.dreadblade.jlibo.domain.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    private static final String BOOKS_DIRECTORY = "/books";
    private static final String IMAGES_DIRECTORY = "/images";

    public static String saveBookFile(MultipartFile book, String uploadPath) throws IOException {
        if (book != null && !book.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath + BOOKS_DIRECTORY);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String resultFilename = uuid + "." + book.getOriginalFilename();

            book.transferTo(new File(uploadDir.toString() + "/" + resultFilename));

            return resultFilename;
        }
        throw new RuntimeException("Error when saving book file!");
    }

    public static String saveBookCoverFile(MultipartFile image, String uploadPath) throws IOException {
        if (image != null && !image.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath + IMAGES_DIRECTORY);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String resultFilename = uuid + "." + image.getOriginalFilename();

            image.transferTo(new File(uploadDir.toString() + "/" + resultFilename));

            return resultFilename;
        }
        throw new RuntimeException("Error when saving image file!");
    }
}
