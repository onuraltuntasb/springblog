package com.onuraltuntas.springblog.repository;

import com.onuraltuntas.springblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Set<Post> findPostsByTagsIn(Set<Long> tags);
}
