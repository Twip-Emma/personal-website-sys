package top.twip.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.twip.market.service.ProductService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 七画一只妖
 * @Date: 2022-04-20 19:14
 */
@RestController
@RequestMapping("/market")
public class TestController {

    @Resource
    private ProductService productService;

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/hello")
    public String hello(HttpServletRequest request) {
        logger.info("请求头TEST {}", request.getHeader("TEST"));
        logger.info("请求头TEST2 {}", request.getHeader("TEST2"));
        return "hello market";
    }

    @GetMapping("/getAllProduct")
    public Object getAllProduct() {
        return null;
    }
}
