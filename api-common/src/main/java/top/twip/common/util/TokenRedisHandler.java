package top.twip.common.util;

import io.jsonwebtoken.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.twip.common.entity.user.WebsiteUserInfo;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class TokenRedisHandler {

    private static final Integer TOKEN_TIME = 1000*60*60; // 有效时间10分钟

    private static final String SIGN = "user";

    private static final String REDIS_KEY_PREFIX = "user_token_"; // redis key前缀

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 获取token
    public String getToken(WebsiteUserInfo userInfo){
        JwtBuilder builder = Jwts.builder();
        String token = builder
                // 头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 载荷
                .claim("id", userInfo.getId())
                .claim("card", userInfo.getCard())
                .claim("pass", userInfo.getPass())
                .claim("admin", userInfo.getIsadmin())
                .setSubject("user-info")
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                // 签名
                .signWith(SignatureAlgorithm.HS256, SIGN)
                .compact();
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + userInfo.getId(), token, TOKEN_TIME, TimeUnit.MILLISECONDS);
        return token;
    }

    // 验证token是否合法和是否过期
    public boolean validateToken(String token) {
        // 验证是否合法、验证是否过期
        try {
            JwtParser parser = Jwts.parser();
            Jws<Claims> claimsJws = parser.setSigningKey(SIGN).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String key = REDIS_KEY_PREFIX + claims.get("id");
            Object tokenFromRedis = redisTemplate.opsForValue().get(key);
            if (tokenFromRedis == null || !token.equals(tokenFromRedis.toString())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        // 验证通过
        try {
            Jwts.parser().setSigningKey(SIGN).parseClaimsJws(token);
        } catch (JwtException ex) {
            return false;
        }
        return true;
    }

    // 验证token中的admin字段是否为0
    public boolean isAdmin(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SIGN).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            Integer admin = (Integer) body.get("admin");
            if (admin == 1) {
                return true;
            }
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // 根据token取出userId
    public String getIdByToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SIGN).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            return (String) body.get("id");
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
