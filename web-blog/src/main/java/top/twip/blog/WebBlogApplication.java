package top.twip.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: 七画一只妖
 * @Date: 2022-04-20 18:42
 */
@MapperScan("top.twip.blog.dao")
@EnableFeignClients(basePackages = "top.twip.api.feign")
@SpringBootApplication(scanBasePackages = {"top.twip.blog", "top.twip.api.globalconfig", "top.twip.api.util"})
public class WebBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebBlogApplication.class, args);
    }

}
