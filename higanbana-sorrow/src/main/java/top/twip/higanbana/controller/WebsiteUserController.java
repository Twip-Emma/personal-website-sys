package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.common.entity.user.UserInfo;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.WebsiteSingleUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:29
 */
@RestController
@RequestMapping("/higanbana/blog/user")
public class WebsiteUserController {

    @Resource
    private WebsiteSingleUserService websiteSingleUserService;

    // 用户登录
    @PostMapping("/login")
    public Object login(@RequestBody WebsiteUserInfo websiteUserInfo,
                        HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "登录成功力")
                .parseData(websiteSingleUserService.userLogin(
                        websiteUserInfo.getCard(),
                        websiteUserInfo.getPass()
                ));
    }


    @PostMapping("/register")
    public Object register(@RequestBody WebsiteUserInfo websiteUserInfo,
                           HttpServletRequest request) throws Exception{
        System.out.println(websiteUserInfo);
        return DataFactory.success(SimpleData.class, "注册成功")
                .parseData(websiteSingleUserService.userRegister(
                        websiteUserInfo.getNickname(),
                        websiteUserInfo.getCard(),
                        websiteUserInfo.getPass()
                ));
    }

    // 根据用户ID查询这个用户实体
    @GetMapping("/getbyid")
    public Object getWebsiteUserById(@RequestParam("userid")String userId,
                                     HttpServletRequest request)throws Exception{
        return null;
    }
}
