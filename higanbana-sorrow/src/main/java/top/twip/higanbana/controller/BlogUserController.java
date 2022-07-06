package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.twip.common.constant.FeignConstants;
import top.twip.common.entity.user.UserInfo;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-05 16:19
 */
@RestController
@RequestMapping("/higanbana/blog")
public class BlogUserController {
    // 用户登录
    @PostMapping("/login")
    public Object login(@RequestBody UserInfo userInfo,
                        HttpServletRequest request){
        return DataFactory.success(SimpleData.class, "登录成功力").parseData(userInfo);
    }
}
