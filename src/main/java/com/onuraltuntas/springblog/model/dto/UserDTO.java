package com.onuraltuntas.springblog.model.dto;

import com.onuraltuntas.springblog.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private int totalBooksCheckedout;
    private String role;
    private User.UserStatus status;
}
