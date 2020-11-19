package com.dreadblade.jlibo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = { "id", "name" })
@ToString(of = { "id", "name" })
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Author's name cannot be empty")
    @Length(max = 128, message = "Author's name is too long (more than 128)")
    private String name;

    @NotBlank(message = "Author's description cannot be empty")
    @Length(max = 2048, message = "Author's description is too long (more than 2048)")
    private String description;
    private String imageFilename;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Book> books;
}
