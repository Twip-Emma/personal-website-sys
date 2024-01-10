package top.twip.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 七画一只妖
 * @Date: 2022-04-20 20:24
 */
@SpringBootApplication(scanBasePackages = {"top.twip.gateway", "top.twip.api.util"})
public class WebGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebGatewayApplication.class, args);
    }
}
