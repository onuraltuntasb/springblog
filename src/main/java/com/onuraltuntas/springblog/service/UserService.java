package com.onuraltuntas.springblog.service;


import com.onuraltuntas.springblog.entity.User;
import com.onuraltuntas.springblog.model.payload.request.TokenRefreshRequest;
import com.onuraltuntas.springblog.model.payload.response.TokenRefreshResponse;
import com.onuraltuntas.springblog.model.payload.response.UserAuthResponse;

public interface UserService {
    User registerUser(User user, boolean authenticated);
    User loginUser(User user);
    UserAuthResponse setUserOtherParams(User user, boolean authenticated);
    TokenRefreshResponse getTokenRefreshResponse(TokenRefreshRequest request);
    User updateUser(User user,Long userId);
    void deleteUser(Long userId);

}
