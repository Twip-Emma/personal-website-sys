package top.twip.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.twip.api.constant.AdminConstants;
import top.twip.api.constant.CurrencyConstants;
import top.twip.api.util.TokenRedisHandler;

import javax.annotation.Resource;

/**
 * @Author: 七画一只妖
 * @Date: 2022-06-22 15:14
 */
//@Order(-1)
@Configuration
public class AuthorizeFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        String reqUrlPath = request.getURI().getPath();

        // 获取请求头中的token
        String token = request.getHeaders().getFirst(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        logger.info("请求[路径={},TOKEN={}]", reqUrlPath, token);

        switch (reqUrlPath) {
            // 直接放行的接口
            case "/blog/blog/user/login", "/blog/blog/user/register", "/blog/meme/query" -> {
                return chain.filter(exchange);
            }

            // 需要判断弱密钥的接口
            case "/blog/image/getRandomImageByType" -> {
                String salt = request.getHeaders().getFirst(AdminConstants.CURRENCY_HEADER_NAME.getValue());
                if (AdminConstants.CURRENCY_HEADER_VALUE.getValue().equals(salt)) {
                    return chain.filter(exchange);
                } else {
                    exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
                }
            }

            // 其它接口，正常走流程验证token
            default -> {
                try {
                    if (reqUrlPath.contains("api")) {
                        exchange.getResponse().setStatusCode((HttpStatus.FORBIDDEN));
                    } else if (isAdminPath(reqUrlPath) && tokenRedisHandler.isAdmin(token)) {
                        return chain.filter(exchange);
                    } else if (tokenRedisHandler.validateToken(token)) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
                    }
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
                }
                return exchange.getResponse().setComplete();
            }
        }
        return exchange.getResponse().setComplete();
    }

    // 判断是否是管理员相关路径
    private boolean isAdminPath(String path) {
        return path.equals("/blog/blog/user/getalluser") ||
                path.equals("/blog/api/addsetukey") ||
                path.equals("/blog/api/deletesetukey");
    }
}
