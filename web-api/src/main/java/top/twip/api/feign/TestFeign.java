package top.twip.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "web-market-service")
public interface TestFeign {

    @GetMapping(value = "/market/hello")
    String hello();
}
