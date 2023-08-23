package com.example.personal_blog.jwt;

import com.example.personal_blog.config.UserDetailsImp;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String generateJwt(UserDetailsImp userDetailsImp) {
        return Jwts.builder()
                .setSubject(userDetailsImp.getUsername())
                .claim("listRoleName", userDetailsImp.getAuthorities())
                .claim("user", userDetailsImp.getUser())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 36000000L))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public Claims getClaimsFromJwt(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    public boolean isExpirationJwt(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    public String getUserNameFromJwt(String jwt) {
        Claims claims = this.getClaimsFromJwt(jwt);
        if (claims == null) {
            throw new BadCredentialsException("invalid jwt");
        }
        if (this.isExpirationJwt(claims)) {
            return claims.getSubject();
        }
        throw new CredentialsExpiredException("expiration jwt");
    }

    public boolean validateJwt(String jwt, HttpServletResponse httpServletResponse) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            httpServletResponse.setStatus(401);
        }
        return false;
    }
}
