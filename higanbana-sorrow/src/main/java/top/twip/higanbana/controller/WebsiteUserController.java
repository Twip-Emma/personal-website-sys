package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.entity.user.UserInfo;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.enums.CodeEnum;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.common.util.TokenHandler;
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

    @Resource
    private TokenHandler tokenHandler;

    /**
     * 用户登录
     * @param websiteUserInfo 用户实体
     * @return Object 用户实体
     */
    @PostMapping("/login")
    public Object login(@RequestBody WebsiteUserInfo websiteUserInfo) throws Exception{
        return DataFactory.success(SimpleData.class, "登录成功力")
                .parseData(websiteSingleUserService.userLogin(
                        websiteUserInfo.getCard(),
                        websiteUserInfo.getPass()
                ));
    }

    /**
     * 用户注册
     * @param websiteUserInfo 用户实体
     * @return Object 用户实体
     */
    @PostMapping("/register")
    public Object register(@RequestBody WebsiteUserInfo websiteUserInfo) throws Exception{
        return DataFactory.success(SimpleData.class, "注册成功")
                .parseData(websiteSingleUserService.userRegister(
                        websiteUserInfo.getNickname(),
                        websiteUserInfo.getCard(),
                        websiteUserInfo.getPass()
                ));
    }

    /**
     * 更新用户信息
     * @param websiteUserInfo 用户实体
     * @return Object 用户实体
     */
    @PostMapping("updateuser")
    public Object updateUser(@RequestBody WebsiteUserInfo websiteUserInfo,
                           HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "注册成功")
                .parseData(websiteSingleUserService.updateUser(websiteUserInfo, token));
    }

    /**
     * 删除一个用户
     * @param websiteUserInfo 用户实体
     * @return Object bool是否删除成功
     */
    @PostMapping("deleteuser")
    public Object deleteUser(@RequestBody WebsiteUserInfo websiteUserInfo){
        try{
            Boolean i = websiteSingleUserService.deleteUser(websiteUserInfo.getId());
            if (i){
                return DataFactory.success(SimpleData.class,"成功删除");
            }else {
                return DataFactory.fail(CodeEnum.NOT_ALL_OK, "删除失败，可能是没这个数据");
            }
        }catch (Exception e){
            return DataFactory.fail(CodeEnum.INTERNAL_ERROR, "失败，数据库服务器出现异常");
        }
    }

    /**
     * 根据用户token查询用户实体
     * @return Object 用户实体
     */
    @GetMapping("/getbytoken")
    public Object getWebsiteUserById(HttpServletRequest request)throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleUserService.getUserByToken(token));
    }

    /**
     * 查询所有头像
     * @return Object 头像列表
     */
    @GetMapping("/getallavatar")
    public Object getAllAvatar()throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleUserService.getAllAvatar());
    }

    /**
     * 获取所有用户
     * @return Object 用户列表
     */
    @GetMapping("/getalluser")
    public Object getAllUser() throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleUserService.getAllUser());
    }
}
