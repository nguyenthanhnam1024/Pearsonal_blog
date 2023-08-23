package com.example.personal_blog.jwt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class ExtractDataFromJwt {
    private final AuthFilter authFilter;
    private final JwtUtils jwtUtils;

    public String extractUserName(HttpServletRequest request) {
        return jwtUtils.getUserNameFromJwt(authFilter.getJwtFromRequest(request));
    }
}
