package top.twip.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.twip.api.constant.AdminConstants;
import top.twip.api.constant.CurrencyConstants;
import top.twip.api.entity.user.WebsiteUserInfo;
import top.twip.api.exception.BadRequestDataException;
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
    private TokenRedisHandler tokenRedisHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        String reqUrlPath = request.getURI().getPath();

        // 获取请求头中的token
        String token = request.getHeaders().getFirst(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());

        // 打印日志
        boolean logging = handleTokenLogging(token, reqUrlPath);
        if (!logging) {
            exchange.getResponse().setStatusCode((HttpStatus.BAD_GATEWAY));
            return exchange.getResponse().setComplete();
        }

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
                    logger.warn("弱密钥不通过请求-可能是被盗链[路径={}]", reqUrlPath);
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

    /**
     * 打印本次请求相关日志，当无法解析时返回false
     *
     * @param token      token
     * @param reqUrlPath 路径
     */
    private boolean handleTokenLogging(String token, String reqUrlPath) {
        if (StringUtils.isNotBlank(token)) {
            WebsiteUserInfo user;
            try {
                user = tokenRedisHandler.getUserByToken(token);
            } catch (BadRequestDataException e) {
                return false;
            }
            logger.info("请求[用户={}, 账号={}, ID={}, 路径={}]",
                    user.getNickname(),
                    user.getCard(),
                    user.getId(),
                    reqUrlPath
            );
        } else {
            logger.info("无token请求[路径={}]",
                    reqUrlPath
            );
        }
        return true;
    }
}
