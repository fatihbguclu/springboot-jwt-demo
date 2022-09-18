package com.jwt.demo.security;

import com.jwt.demo.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        try{
            if (token != null && jwtProvider.validateToken(token)){
                Authentication auth = jwtProvider.getAuth(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (CustomException ex){
            SecurityContextHolder.clearContext();
            response.sendError(ex.getStatus().value(), ex.getMessage());
            return;
        }

        filterChain.doFilter(request,response);

    }

}
