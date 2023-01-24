package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PostServiceImplTest {

    @InjectMocks
    private  PostServiceImpl underTest;

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp(){
        underTest = new PostServiceImpl(postRepository,userRepository);
    }

    @Test
    void canSavePost() {

        Date date = new Date();

        PostRequest postRequest = new PostRequest();
        postRequest.setHeader("header1");
        postRequest.setContent("content1");
        postRequest.setCreateDate(date);
        postRequest.setLastUpdateDate(date);
        postRequest.setTags(null);

        Set<Post> postSet = new HashSet<>();

        User user = User.builder()
                .id(1L)
                .name("onur1234")
                .email("onuraltuntas50@gmail.com")
                .status(User.UserStatus.ACTIVE)
                .roles(null)
                .posts(postSet)
                .phone("5075985053")
                .password("duYKEyA59*4&")
                .build();
        // when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any())).then(returnsFirstArg());
        Post post = underTest.savePost(postRequest, 1L);

        //then
        verify(postRepository,times(1)).save(post);

    }

    @Test
    void updatePost() {

        Date date = new Date();
        Post post = Post.builder()
                .id(1L)
                .header("header1")
                .content("content1")
                .creationDate(date)
                .lastUpdateDate(date)
                .likeCount(0)
                .dislikeCount(0)
                .popular(0.0)
                .build();

        // when
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        underTest.updatePost(post, 1L);

        //then
        verify(postRepository,times(1)).save(post);

    }
}