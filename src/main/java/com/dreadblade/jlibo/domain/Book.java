package com.dreadblade.jlibo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "books")
@Data
@EqualsAndHashCode(of = { "id", "title" })
@ToString(of = { "id", "title" })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    private User uploadedBy;

    private String imageFilename;
    private String bookFilename;
}
