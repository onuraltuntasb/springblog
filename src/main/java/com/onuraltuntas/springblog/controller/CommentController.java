package com.onuraltuntas.springblog.controller;


import com.onuraltuntas.springblog.entity.Comment;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.payload.request.CommentRequest;
import com.onuraltuntas.springblog.repository.CommentRepository;
import com.onuraltuntas.springblog.security.JwtUtils;
import com.onuraltuntas.springblog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final JwtUtils jwtUtils;


    @PostMapping("/save")
    public ResponseEntity<?> saveComment(@Valid @RequestBody CommentRequest commentRequest,
                                         @RequestParam(value = "user-id")Long userId,@RequestParam(value = "post-id")Long postId,
                                         @RequestParam(value = "comment-id")Long commentId){

        if(userId == null || postId ==null || commentId ==null){
            System.out.println("postId" + postId);
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(commentService.saveComment(commentRequest,userId,postId,commentId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestParam(value = "comment-id")Long commentId,
                                           @RequestHeader (name="Authorization") String token){

        if(commentId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }
        Comment comment1 = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment is not found with this id : "+ commentId));

        String auth = jwtUtils.getAuthorityClaim(token);
        if(comment1.getUser().getEmail().equals(jwtUtils.extractUsername(token.substring(7))) || auth.equals("ROLE_ADMIN") ){
            commentRepository.deleteById(commentId);

            return ResponseEntity.ok().body("success");
        }else{
            return ResponseEntity.badRequest().body("You are not allowed to this action!");
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateComment(@Valid @RequestBody CommentRequest commentRequest,
                                           @RequestParam(value = "user-id")Long userId,@RequestParam(value = "post-id")Long postId,
                                           @RequestParam(value = "comment-id")Long commentId,
                                           @RequestHeader (name="Authorization") String token){

        if(userId == null || postId == null || commentId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        Comment comment1 = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment is not found with this id : "+ commentId));

        String auth = jwtUtils.getAuthorityClaim(token);

        if(comment1.getUser().getEmail().equals(jwtUtils.extractUsername(token.substring(7)))|| auth.equals("ROLE_ADMIN") ){
            return ResponseEntity.ok().body(commentService.updateComment(commentRequest,userId,postId,commentId));
        }else{
            return ResponseEntity.badRequest().body("You are not allowed to this action!");
        }

    }

}
