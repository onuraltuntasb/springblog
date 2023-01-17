package com.onuraltuntas.springblog.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequest {

private String email;
private String name;
private String password;

}
