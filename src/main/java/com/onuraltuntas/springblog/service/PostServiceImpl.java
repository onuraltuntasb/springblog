package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post updatePost(Post post, Long postId) {

    Post rPost = postRepository.findById(postId).orElseThrow(
            ()->new ResourceNotFoundException("Post not found with this id : "+ postId)
    );

    rPost.setContent(post.getContent());
    rPost.setHeader(post.getContent());
    rPost.setTags(post.getTags());

    return postRepository.save(rPost);
    }

    @Override
    public Post savePost(PostRequest postRequest, Long userId) {

        Date date = new Date();
        Post post = Post.builder()
                .header(postRequest.getHeader())
                .content(postRequest.getContent())
                .creationDate(date)
                .lastUpdateDate(date)
                .likeCount(0)
                .dislikeCount(0)
                .popular(0.0)
                .build();

        User user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User not found with this id : "+ userId)
        );

        user.getPosts().add(post);
        user.setPosts(user.getPosts());

        return postRepository.save(post) ;
    }
}
