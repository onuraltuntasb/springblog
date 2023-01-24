package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.Tag;
import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.payload.request.PostListIdRequest;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.repository.TagRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Override
    public Post updatePost(Post post, Long postId) {

    Post rPost = postRepository.findById(postId).orElseThrow(
            ()->new ResourceNotFoundException("Post not found with this id : "+ postId)
    );

    rPost.setContent(post.getContent());
    rPost.setHeader(post.getHeader());
    rPost.setTags(post.getTags());

    return postRepository.save(rPost);
    }

    @Override
    public Post savePost(PostRequest postRequest, Long userId) {



        List<PostListIdRequest> requestPostIds = postRequest.getTags();
        Set<Tag> tags = new HashSet<>();

        if(requestPostIds!=null){
            for (int i = 0; i < requestPostIds.size();i++){
                log.info("postId : {}",requestPostIds.get(i).getId());

                Long rId =requestPostIds.get(i).getId();
                tags.add(tagRepository.findById(rId)
                        .orElseThrow(()-> new ResourceNotFoundException("Tag not found with this id : "+rId )));

            }
        }


        Date date = new Date();

        Post post = Post.builder()
                .header(postRequest.getHeader())
                .content(postRequest.getContent())
                .creationDate(date)
                .lastUpdateDate(date)
                .likeCount(0)
                .dislikeCount(0)
                .popular(0.0)
                .tags(tags)
                .build();



        User user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User not found with this id : "+ userId)
        );

        user.getPosts().add(post);

        return postRepository.save(post) ;
    }
}
