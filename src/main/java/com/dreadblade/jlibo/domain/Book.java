package com.dreadblade.jlibo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@EqualsAndHashCode(of = { "id", "title" })
@ToString(of = { "id", "title" })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Book's title cannot be empty")
    @Length(max = 128, message = "Book's title is too long (more than 128)")
    private String title;

    private boolean isAccepted;

    private String imageFilename;
    private String bookFilename;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User uploadedBy;

    @ManyToMany
    @JoinTable(
            name = "book_likes",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> likes;
}
