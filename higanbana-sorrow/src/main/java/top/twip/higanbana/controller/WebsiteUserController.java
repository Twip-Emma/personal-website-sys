package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/login")
    public Object login(@RequestBody WebsiteUserInfo websiteUserInfo,
                        HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "登录成功力")
                .parseData(websiteSingleUserService.userLogin(
                        websiteUserInfo.getCard(),
                        websiteUserInfo.getPass()
                ));
    }
}
