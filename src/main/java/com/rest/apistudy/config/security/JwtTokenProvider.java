package com.rest.apistudy.config.security;


import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component

public class JwtTokenProvider {
    @Value("spring.jwt.secret")
    private String secretKey;

//    private long tokenValidMillisecond = 1000L * 60 * 60;

    private final UserDetailsService userDetailsService;



    /**
     * 시크릿키 Base64으로 인코딩
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }



    /**
     *JWT토큰 생성
     * DefaultJwtBuilder안에 DefaultClaims 있다
     * DefaultJwtBuilder으로 계약완성시킨다.
     */
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims()//new DefaultClaims
                .setSubject(userPk);
        claims.put("roles", roles);
        return Jwts.builder() //DefaultJwtBuilder
                .setClaims(claims)//DefaultJwtBuilder //DefaultJwtBuilder안에 Claims에  DefaultClaims로 구현된 claims 주입
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }



    /**
     * Jwt 토큰에서 인증 정보를 조회
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Jwt 토큰에서 회원 구별 정보 추출
     */
    private String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * Jwt 토큰의 유효성 + 만료일자 확인
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return claims.getBody().getExpiration().before(Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception e) {
            return false;
        }

    }



}
