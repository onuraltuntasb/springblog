package com.onuraltuntas.springblog.controller;


import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.dto.PostDTO;
import com.onuraltuntas.springblog.model.dto.UserDTO;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import com.onuraltuntas.springblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<?> savePost(@Valid @RequestBody PostRequest postRequest,@RequestParam(value = "user-id")Long userId){

        if(userId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(postService.savePost(postRequest,userId));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@Valid @RequestBody Post post,@RequestParam(value = "post-id")Long postId
    ,@RequestParam(value = "user-id")Long userId){

        if(userId == null || postId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        PostDTO postDTO = modelMapper.map(postService.updatePost(post,postId), PostDTO.class);

        return ResponseEntity.ok().body(postDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam(value = "post-id")Long postId){

        if(postId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        postRepository.deleteById(postId);

        return ResponseEntity.ok().body("success");

    }

}
