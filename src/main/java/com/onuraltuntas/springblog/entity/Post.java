package com.onuraltuntas.springblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Entity
@Getter
@Setter
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

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade ={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="post_tag",
            joinColumns = {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="tag_id")})
    private Set<Tag> tags = new HashSet<>();


    @Transactional
    public void removeTag(Long tagId) {

        System.out.println("bak");
        System.out.println(this.tags);


        Tag tag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
        if (tag != null) {
            System.out.println("hey");
            System.out.println(tag);

            this.tags.remove(tag);
            tag.getPosts().remove(this);
        }
    }

}
