package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.RefreshToken;
import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.exception.TokenRefreshException;
import com.onuraltuntas.springblog.model.payload.request.TokenRefreshRequest;
import com.onuraltuntas.springblog.model.payload.response.TokenRefreshResponse;
import com.onuraltuntas.springblog.model.payload.response.UserAuthResponse;
import com.onuraltuntas.springblog.repository.RefreshTokenRepository;
import com.onuraltuntas.springblog.repository.RoleRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import com.onuraltuntas.springblog.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;


    @Override
    public User registerUser(User user, boolean authenticated) {

        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        //set other user fields
        user.setStatus(User.UserStatus.ACTIVE);
        user.setPhone(user.getPhone());

        //encode password
        String plainPassword = user.getPassword();
        if(plainPassword!=null){
            user.setPassword(passwordEncoder.encode(plainPassword));
        }else{
            throw new ResourceNotFoundException("User password not found!");
        }

            User rUser =null;

            //save user
            rUser = userRepository.save(user);


        return rUser;
    }

    @Override
    public User loginUser(User user) {

        //find user
        User rUser = (User) userRepository
                .findUserByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("Not found email with id = " + user.getEmail()));

        //check username
        if(!rUser.getName().equals(user.getName())){
            //just throw bad credentials for security purposes
            log.error("username is wrong!");
            throw new BadCredentialsException("Bad credentials!");
        }

        //authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),user.getPassword()
                ));


        return rUser;
    }

    public UserAuthResponse setUserOtherParams(User user, boolean authenticated){

        //find userDetails
        UserDetails userDetails = (UserDetails) userRepository
                .findUserByEmail(user.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Not found email with id = " + user.getEmail()));

        RefreshToken refreshToken = null;

        if(authenticated){
             refreshToken = refreshTokenService
                    .createRefreshToken(user.getId());
        }
        try {
            UserAuthResponse userAuthResponse = new UserAuthResponse();
            userAuthResponse = UserAuthResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .authorities(user.getAuthorities())
                    .jwtToken(jwtUtils.generateToken(userDetails))
                    .jwtRefreshToken(refreshToken.getToken())
                    .build();
            return userAuthResponse;
        }catch (NullPointerException e){
            log.error("userDetails or user possibly null!");
            return null;
        }
    }

    @Override
    public TokenRefreshResponse getTokenRefreshResponse(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        TokenRefreshResponse tokenRefreshResponse = refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user);
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));

        return tokenRefreshResponse;

    }

    @Override
    public User updateUser(User user, Long userId) {

        User rUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with this id:"+userId
                ));

        if(rUser!=null){

            //update just these fields
            rUser.setName(user.getName());
            rUser.setPhone(user.getPhone());

            //TODO before user status update need to finish other related tasks
            //rUser.setStatus(user.getStatus());
        }

        return  userRepository.save(rUser);
    }

    @Override
    public void deleteUser(Long userId) {

        User rUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with this id:"+userId
                ));

        refreshTokenRepository.deleteById(userId);

        //TODO before delete need to finish other related tasks
        userRepository.deleteById(userId);

    }
}
