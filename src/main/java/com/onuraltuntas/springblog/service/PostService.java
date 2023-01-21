package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;

public interface PostService {
    Post updatePost(Post post,Long postId);
    Post savePost(PostRequest postRequest, Long userId);
}
