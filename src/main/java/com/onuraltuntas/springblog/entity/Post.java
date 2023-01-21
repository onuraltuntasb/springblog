package com.onuraltuntas.springblog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String header;

    @NotEmpty
    private String content;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastUpdateDate;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",referencedColumnName = "id")
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",referencedColumnName = "id")
    private Set<Comment> comments = new HashSet<>();

    @NotNull
    @Min(0)
    private int likeCount;

    @NotNull
    @Min(0)
    private int dislikeCount;

    @NotNull
    private double popular = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
