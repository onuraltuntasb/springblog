package com.onuraltuntas.springblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String userName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date creationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date lastUpdateDate;

    @NotBlank
    private String content;

    @NotNull
    @Min(0)
    private int likeCount;

    @NotNull
    @Min(0)
    private int dislikeCount;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade ={CascadeType.PERSIST,CascadeType.MERGE}
          )
    private User user;


    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,cascade = CascadeType.ALL )
    @JoinColumn(name = "comment_id",referencedColumnName = "id")
    private Set<Comment> comments = new HashSet<>();

}
