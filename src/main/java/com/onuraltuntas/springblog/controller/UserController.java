package com.onuraltuntas.springblog.controller;


import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.model.dto.UserDTO;
import com.onuraltuntas.springblog.model.payload.request.CheckAuthRequest;
import com.onuraltuntas.springblog.model.payload.request.TokenRefreshRequest;
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
    public ResponseEntity<?> loginUser(@RequestBody User user){

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
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,@RequestParam(value = "id",required = true)Long id){
        log.info("id : {}",id);
        if(id==null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        UserDTO userDTO = modelMapper.map(userService.updateUser(user,id), UserDTO.class);

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(value = "id",required = true)Long id){
        if(id==null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        userService.deleteUser(id);

        return ResponseEntity.ok("success");
    }

}

