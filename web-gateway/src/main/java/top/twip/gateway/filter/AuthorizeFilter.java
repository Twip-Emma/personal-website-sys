package top.twip.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 获取请求路径
        String reqUrlPath = request.getURI().getPath();
        System.out.println(reqUrlPath);
        // 代替ip地址以token作为脚本判断依据
        String token = request.getHeaders().getFirst(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());

        // 判断是否是登录界面
        if (reqUrlPath.equals("/blog/blog/user/login")
                || reqUrlPath.contains("api")
                || reqUrlPath.equals("/blog/blog/user/register")
                || reqUrlPath.equals("/blog/meme/query")
                || reqUrlPath.equals("/blog/file/upload/image")) {
            return chain.filter(exchange);
        } else if (reqUrlPath.equals("/blog/blog/user/getalluser")
                || reqUrlPath.equals("/blog/api/addsetukey")
                || reqUrlPath.equals("/blog/api/deletesetukey")) {
            // 判断是否有管理员权限
            try {
                boolean i = tokenRedisHandler.isAdmin(token);
                if (i) {
                    return chain.filter(exchange);
                } else {
                    exchange.getResponse().setStatusCode((HttpStatus.FORBIDDEN));
                }
            } catch (Exception e) {
                exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
            }
        } else {
            // 不是登录/注册页面
            boolean re = tokenRedisHandler.validateToken(token);
            if (re) {
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
            }
        }
        return exchange.getResponse().setComplete();
    }
}
