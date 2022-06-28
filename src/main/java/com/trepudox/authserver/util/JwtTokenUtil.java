package com.trepudox.authserver.util;

import com.trepudox.authserver.dataprovider.dto.JwtResponseDTO;
import com.trepudox.authserver.dataprovider.enums.EnProfile;
import com.trepudox.authserver.dataprovider.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 8300949249285114768L;

    private static final String TOKEN_TYPE = "Bearer";

    private static final Long JWT_TOKEN_VALIDITY = 60L * 60 ; // in minutes

    @Value("${security.jwt.secret}")
    private String secret;

    public JwtResponseDTO generateToken(UserModel user) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = EnProfile.getRoles(user.getProfile());
        claims.put("roles", roles);

        String jwtToken = doGenerateToken(claims, user.getUsername());

        return new JwtResponseDTO(jwtToken, TOKEN_TYPE, JWT_TOKEN_VALIDITY, roles);
    }

    public void validateToken(String token) {
        Jwts.parser().setSigningKey(secret.trim().getBytes()).parseClaimsJws(token.trim());
    }

    public String getSubjectFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getTokenExpirationDate(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public List<String> getTokenRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getTokenExpirationDate(token.trim());
        return expiration.before(new Date());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.trim().getBytes()).parseClaimsJws(token.trim()).getBody();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret.trim().getBytes())
                .compact();
    }

}