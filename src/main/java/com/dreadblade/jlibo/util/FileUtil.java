package com.dreadblade.jlibo.util;

import com.dreadblade.jlibo.domain.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    private static final String BOOKS_DIRECTORY = "/books";
    private static final String IMAGES_DIRECTORY = "/images";
    private static final String AUTHOR_DIRECTORY = "/author";

    public static enum TypeOfFile {
        BOOK_FILE("/books"),
        BOOK_IMAGE("/images"),
        AUTHOR_IMAGE("/author");

        private String directory;

        TypeOfFile(String directory) {
            this.directory = directory;
        }

        public String getDirectory() {
            return directory;
        }
    }

    public static String saveFile(MultipartFile book, String uploadPath, TypeOfFile type) throws IOException {
        if (book != null && !book.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath +type.getDirectory());

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String resultFilename = uuid + "." + book.getOriginalFilename();

            book.transferTo(new File(uploadDir.toString() + "/" + resultFilename));

            return resultFilename;
        }
        throw new RuntimeException("Error when saving file!");
    }
}
