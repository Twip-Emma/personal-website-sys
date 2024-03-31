package top.twip.market.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.twip.market.service.ProductService;
import top.twip.api.response.DataFactory;
import top.twip.api.response.SimpleData;

import javax.annotation.Resource;

/**
 * @Author: 七画一只妖
 * @Date: 2022-04-20 19:14
 */
@RestController
@RequestMapping("/market")
public class TestController {

    @Resource
    private ProductService productService;

    @RequestMapping("/hello")
    public String hello() {
        return "hello cherry";
    }

    @GetMapping("/getAllProduct")
    public Object getAllProduct() {
        return null;
    }
}
