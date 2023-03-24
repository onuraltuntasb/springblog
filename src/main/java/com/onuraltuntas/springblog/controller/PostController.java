package com.onuraltuntas.springblog.controller;


import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.dto.PostDTO;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.security.JwtUtils;
import com.onuraltuntas.springblog.service.PostService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.JwtMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;




    @PostMapping("/save")
    public ResponseEntity<?> savePost(@Valid @RequestBody PostRequest postRequest,@RequestParam(value = "user-id")Long userId){

        if(userId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(postService.savePost(postRequest,userId));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostRequest post,@RequestParam(value = "post-id")Long postId
    ,@RequestParam(value = "user-id")Long userId,@RequestHeader (name="Authorization") String token){




        if(userId == null || postId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

       Post post1 = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post is not found with this id : "+ postId));

        String auth = jwtUtils.getAuthorityClaim(token);

        if(post1.getUser().getEmail().equals(jwtUtils.extractUsername(token.substring(7))) || auth.equals("ROLE_ADMIN") ){
            PostDTO postDTO = modelMapper.map(postService.updatePost(post,postId), PostDTO.class);
            return ResponseEntity.ok().body(postDTO);
        }else{
            return ResponseEntity.badRequest().body("You are not allowed to this action!");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam(value = "post-id")Long postId,@RequestHeader (name="Authorization") String token){

        if(postId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        Post post1 = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post is not found with this id : "+ postId));


        String auth = jwtUtils.getAuthorityClaim(token);

        if(post1.getUser().getEmail().equals(jwtUtils.extractUsername(token.substring(7))) || auth.equals("ROLE_ADMIN") ){
            postRepository.deleteById(postId);
            return ResponseEntity.ok().body("success");
        }else{
            return ResponseEntity.badRequest().body("You are not allowed to this action!");
        }

    }

}
