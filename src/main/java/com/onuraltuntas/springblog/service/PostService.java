package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;

public interface PostService {
    Post updatePost(PostRequest post,Long postId);
    Post savePost(PostRequest postRequest, Long userId);
}
