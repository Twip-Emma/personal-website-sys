package top.twip.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.constant.FeignConstants;
import top.twip.common.constant.NoValueConstants;
import top.twip.common.util.TokenHandler;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private TokenHandler tokenHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取请求路径
        String reqUrlPath = request.getURI().getPath();
        System.out.println(reqUrlPath);
        // 获取ip地址
        String hostAddress = request.getRemoteAddress().getAddress().getHostAddress();
        // 代替ip地址以token作为脚本判断依据
        String token = request.getHeaders().getFirst(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());

        // redis查询
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // 判断是否是登录界面
        if (reqUrlPath.equals("/higanbana/blog/user/login")
                || reqUrlPath.contains("api")
                || reqUrlPath.equals("/higanbana/blog/user/register")
                || reqUrlPath.equals("/higanbana/blog/user/checktoken")) {
            return chain.filter(exchange);
        }else if(reqUrlPath.equals("/higanbana/blog/user/getalluser")
                || reqUrlPath.equals("/higanbana/api/addsetukey")
                || reqUrlPath.equals("/higanbana/api/deletesetukey")){
            // 判断是否有管理员权限
            try{
                tokenHandler.checkToken(token);
                Boolean i = tokenHandler.checkTokenIsAdmin(token);
                if (i){
                    return chain.filter(exchange);
                }else {
                    exchange.getResponse().setStatusCode((HttpStatus.FORBIDDEN));
                }
            } catch (Exception e){
                exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
            }

        }else{
            // 不是登录/注册页面
            try{
                tokenHandler.checkToken(token);
                return chain.filter(exchange);
            } catch (Exception e){
                exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
            }
        }
        return exchange.getResponse().setComplete();
    }
}
