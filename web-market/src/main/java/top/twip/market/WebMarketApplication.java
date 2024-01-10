package top.twip.market;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("top.twip.market.dao")
@EnableFeignClients(basePackages = "top.twip.api.feign")
@SpringBootApplication(scanBasePackages = {"top.twip.market", "top.twip.api.globalconfig"})
public class WebMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMarketApplication.class, args);
    }

}
