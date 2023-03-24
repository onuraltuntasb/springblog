package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Comment;
import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.payload.request.CommentRequest;
import com.onuraltuntas.springblog.repository.CommentRepository;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService  {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public Comment updateComment(CommentRequest commentRequest, Long userId,Long postId,Long commentId) {

        Comment rComment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment not found with this id : "+ commentId)
        );

        User rUser = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("user not found with this id : "+ userId)
        );

        Date date = new Date();

        rComment.setContent(commentRequest.getContent());
        rComment.setLastUpdateDate(date);
        rComment.setUserName(rUser.getName());


        return commentRepository.save(rComment);
    }

    @Override
    public Comment saveComment(CommentRequest commentRequest, Long userId,Long postId,Long commentId) {

        User user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User not found with this id : "+ userId)
        );

        Post post = postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with this id : "+ userId)
        );

        Date date = new Date();


        Comment rComment = Comment.builder()
                .content(commentRequest.getContent())
                .creationDate(date)
                .lastUpdateDate(date)
                .dislikeCount(0)
                .likeCount(0)
                .userName(user.getName())
                .build();

        Comment comment = null;

        if (commentId != 0) {
            comment = commentRepository.findById(commentId).orElseThrow(
                    ()->new ResourceNotFoundException("Comment not found with this id : "+ userId)
            );

                System.out.println("buralar###");

            post.getComments().add(rComment);
            comment.getComments().add(rComment);
            user.getComments().add(rComment);

            return commentRepository.save(rComment);
        }else{
            post.getComments().add(rComment);
            user.getComments().add(rComment);

            return commentRepository.save(rComment);
        }

    }
}
