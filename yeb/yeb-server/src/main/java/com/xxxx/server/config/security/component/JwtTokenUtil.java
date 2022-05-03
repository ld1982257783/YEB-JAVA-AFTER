package com.xxxx.server.config.security.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

//    根据用户信息生成token
    public String generatorToken(UserDetails userDetails){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generatorToken(claims);
    }


//    从token中获取登录用户名
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFormToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;

        }
        return username;
    }

//    将token反转为荷载
    private Claims getClaimsFormToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            claims = null;
        }
        return  claims;
    }


    //    根据荷载生成JWT TOKEN
    public String generatorToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generatorExpirationDate())
                //加密盐
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }


//    生成有效时间
    public Date generatorExpirationDate(){
        return new Date(System.currentTimeMillis()+expiration+1000);
    }


//    验证token是否有效
    public boolean validateToken(String token,UserDetails userDetails){
        String userName = getUserNameFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }

//    判断token是否失效
    private boolean isTokenExpired(String token) {
        Date expireDate =  getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

//    从token中获取失效时间
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration();
    }


//    判断token是否可以被刷新
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }


//    刷新token方法
    public String RefreshToken(String token){
        Claims claims = getClaimsFormToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generatorToken(claims);
    }
}
