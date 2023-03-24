package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Comment;
import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.model.payload.request.CommentRequest;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;

public interface CommentService {

    Comment updateComment(CommentRequest commentRequest, Long userId,Long postId,Long commentId);
    Comment saveComment(CommentRequest commentRequest, Long userId,Long PostId,Long commentId);


}
