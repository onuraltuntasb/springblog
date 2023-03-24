package com.onuraltuntas.springblog.controller;


import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.model.dto.UserDTO;
import com.onuraltuntas.springblog.model.payload.request.CheckAuthRequest;
import com.onuraltuntas.springblog.model.payload.request.TokenRefreshRequest;
import com.onuraltuntas.springblog.repository.UserRepository;
import com.onuraltuntas.springblog.security.JwtUtils;
import com.onuraltuntas.springblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user){


        return ResponseEntity.ok(
                userService.setUserOtherParams(
                        userService.registerUser(
                                user,true),true
                )
        );
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody User user){

        return ResponseEntity.ok(
                userService.setUserOtherParams(
                        userService.loginUser(user),true
                )
        );
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkAuth(@Valid @RequestBody CheckAuthRequest checkAuthRequest){
        return ResponseEntity.ok(jwtUtils.isTokenValid(checkAuthRequest.getToken(),checkAuthRequest.getEmail()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
       return ResponseEntity.ok(userService.getTokenRefreshResponse(request));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,@RequestParam(value = "id",required = true)Long id,
            @RequestHeader (name="Authorization") String token){



        if(id==null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        User user1 = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post is not found with this id : "+ id));


        String auth = jwtUtils.getAuthorityClaim(token);

        if(user1.getEmail().equals(jwtUtils.extractUsername(token.substring(7))) || auth.equals("ROLE_ADMIN") ){
            UserDTO userDTO = modelMapper.map(userService.updateUser(user,id), UserDTO.class);
            return ResponseEntity.ok(userDTO);
        }else{
            return ResponseEntity.badRequest().body("You are not allowed to this action!");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(value = "id",required = true)Long id
            ,@RequestHeader (name="Authorization") String token){

        if(id==null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        User user1 = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post is not found with this id : "+ id));

        String auth = jwtUtils.getAuthorityClaim(token);

        if(user1.getEmail().equals(jwtUtils.extractUsername(token.substring(7)))|| auth.equals("ROLE_ADMIN") ){
            userService.deleteUser(id);
            return ResponseEntity.ok("success");
        }else{
            return ResponseEntity.badRequest().body("You are not allowed to this action!");
        }

    }

}

