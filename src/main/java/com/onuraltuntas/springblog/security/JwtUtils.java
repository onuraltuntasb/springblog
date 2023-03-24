package com.onuraltuntas.springblog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
@Data
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${onuraltuntas.app.jwtSecret}")
    private String jwtSigningKey;

    @Value("${onuraltuntas.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){return extractClaim(token,Claims::getExpiration);}

    public boolean hasClaim(String token, String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) !=null;
    }

    public String getString(String name,Claims claims) {
        Object v = claims.get(name);
        return v != null ? String.valueOf(v) : null;
    }

    public String getAuthorityClaim(String token){
        Claims claims = extractAllClaims(token.substring(7));
        String authorities = getString("authorities",claims);
        String st = authorities.substring(12);
        String auth =st.substring(0,st.length() -2);

        return auth;
    }





    public<T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getJwtSigningKey()).parseClaimsJws(token).getBody();}


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());}

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userDetails);
    }

    public String createToken(Map<String,Object> claims, UserDetails userDetails){
        log.info("bak bura :{}",userDetails.getAuthorities());

        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())

                //TODO authorities
                .claim("authorities",userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtExpirationMs()))
                .signWith(SignatureAlgorithm.HS256,getJwtSigningKey()).compact();
    }

    public Boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        log.info("jwtUtilsValidName : {}",username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenValid(String token,String email){
        final String username = extractUsername(token);
        log.info("jwtUtilsValidName : {}",username);
        return (username.equals(email) && !isTokenExpired(token));
    }
}
