package top.twip.common.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import top.twip.common.entity.user.WebsiteUserInfo;

import java.util.Date;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 17:22
 */
@Component
public class TokenHandler {
    private static final Integer TOKEN_TIME = 1000*60*60*3; // 有效时间3小时

    private static final String SIGN = "user";

    // 获取token
    public String getToken(WebsiteUserInfo userInfo){
        JwtBuilder builder = Jwts.builder();
        return builder
                // 头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 载荷
                .claim("card", userInfo.getCard())
                .claim("pass", userInfo.getPass())
                .claim("admin", userInfo.getIsadmin())
                .setSubject("user-info")
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                // 签名
                .signWith(SignatureAlgorithm.HS256, SIGN)
                .compact();
    }

    // 解析token
    public void checkToken(String token){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey("user").parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        claims.get("card");
        claims.get("pass");
    }

    // 判断该token是否有admin权限
    public Boolean checkTokenIsAdmin(String token){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey("user").parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (Integer) claims.get("admin") == 1;
    }
}
