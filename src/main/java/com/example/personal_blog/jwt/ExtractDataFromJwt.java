package com.example.personal_blog.jwt;

import com.example.personal_blog.exception.MyValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class ExtractDataFromJwt {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final AuthFilter authFilter;
    private final JwtUtils jwtUtils;

    @Autowired
    public ExtractDataFromJwt(AuthFilter authFilter, JwtUtils jwtUtils) {
        this.authFilter = authFilter;
        this.jwtUtils = jwtUtils;
    }

    public String extractUserName(HttpServletRequest request) {
        return jwtUtils.getUserNameFromJwt(authFilter.getJwtFromRequest(request));
    }

    public List<String> extractListRole(HttpServletRequest request) throws MyValidateException {
        String jwt = authFilter.getJwtFromRequest(request);
        Jws<Claims> claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.getBody().get("listRoleName");
        List<String> listRole = new ArrayList<>();
        if (!authorities.isEmpty()) {
            for (Map<String, String> element: authorities) {
                listRole.add(element.get("authority"));
            }
            return listRole;
        }
        throw new MyValidateException("you have no right");
    }

    public LinkedHashMap<String, Object> extractInfoUser(HttpServletRequest request) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authFilter.getJwtFromRequest(request));
        return  (LinkedHashMap<String, Object>) claimsJws.getBody().get("user");
    }

    public long getUserId(HttpServletRequest request) {
        Map<String, Object> infoUser =  this.extractInfoUser(request);
        Integer userTypeInteger = (Integer) infoUser.get("userId");
        return (long) userTypeInteger;
    }
}
