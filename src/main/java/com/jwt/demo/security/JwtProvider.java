package com.jwt.demo.security;

import com.jwt.demo.exception.CustomException;
import com.jwt.demo.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${application.security.secret-key}")
    private String secretKey;
    @Value("${application.security.expire}")
    private long expireTime;

    private final AppUserDetails userDetails;

    @PostConstruct
    private void init(){
        this.secretKey =  Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public JwtProvider(AppUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getToken(String username, List<Role> roleList){

        Claims claims = Jwts.claims().setSubject(username);

        claims.put("auth",
                roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList())
        );

        Date date = new Date(new Date().getTime() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuth(String token){
        UserDetails userDetails1 = userDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails1, "", userDetails1.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(0,7);
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            throw new CustomException("Invalid JWT Token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
