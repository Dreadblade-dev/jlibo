package com.dreadblade.jlibo.domain.dto;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import lombok.Getter;

@Getter
public class BookDto {
    private Long id;
    private String title;
    private Author author;
    private boolean isAccepted;
    private String imageFilename;
    private String bookFilename;
    private User uploadedBy;
    private Long likes;
    private Boolean meLiked;

    public BookDto(Book book, Long likes, Boolean meLiked) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isAccepted = book.isAccepted();
        this.imageFilename = book.getImageFilename();
        this.bookFilename = book.getBookFilename();
        this.uploadedBy = book.getUploadedBy();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public BookDto(Book book, Long likes) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isAccepted = book.isAccepted();
        this.imageFilename = book.getImageFilename();
        this.bookFilename = book.getBookFilename();
        this.uploadedBy = book.getUploadedBy();
        this.likes = likes;
        this.meLiked = false;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", title=" + title +
                ", author=" + author.getName() +
                ", uploadedBy=" + uploadedBy.getUsername() +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
