import io.jsonwebtoken.*;

import java.util.Date;

public class jwtHandler {

    private static final Integer TOKEN_TIME = 1000*2; // 有效时间30秒

    public static void main(String[] args) {
        JwtBuilder builder = Jwts.builder();
        String token = builder
                // 头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 载荷
                .claim("card", "11111111")
                .claim("pass", "123")
                .setSubject("user-info")
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                // 签名
                .signWith(SignatureAlgorithm.HS256, "user")
                .compact();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey("user").parseClaimsJws(token + "123123");
        Claims claims = claimsJws.getBody();
    }
}
