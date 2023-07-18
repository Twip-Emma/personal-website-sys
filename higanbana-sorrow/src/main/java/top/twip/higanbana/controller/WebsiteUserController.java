package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.constant.FileSizeConstants;
import top.twip.common.entity.file.Constant;
import top.twip.common.entity.user.UserInfo;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.enums.CodeEnum;
import top.twip.common.exception.FileSizeExceededException;
import top.twip.common.response.BaseData;
import top.twip.common.response.DataFactory;
import top.twip.common.response.ListData;
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
        websiteSingleUserService.updateUser(websiteUserInfo, token);
        return DataFactory.success(SimpleData.class, "成功");
    }


    @GetMapping("getUserInfo")
    public Object getUserInfo(HttpServletRequest request) {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        WebsiteUserInfo user = websiteSingleUserService.getUserInfo(token);
        user.setToken(token);
        return DataFactory.success(SimpleData.class, "成功").parseData(user);
    }


    /**
     * 更新用户信息
     * @param websiteUserInfo 用户实体
     * @return Object 用户实体
     */
    @PostMapping("updatealluser")
    public Object updateAllUser(@RequestBody WebsiteUserInfo websiteUserInfo) throws Exception{
        return DataFactory.success(SimpleData.class, "成功")
                .parseData(websiteSingleUserService.updateAllUser(websiteUserInfo));
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
    public Object getAllUser(@RequestParam("page")Integer page,
                             @RequestParam(value = "name", required = false)String name) throws Exception{
//        return DataFactory.success(ListData.class, "查询成功")
//                .parseData(websiteSingleUserService.getAllUser(page))
//                .parseData(websiteSingleUserService.getUserCount());
        BaseData R = DataFactory.success(ListData.class, "查询成功")
                .parseData(websiteSingleUserService.getAllUser(page, name));
        if (name != null && !name.isEmpty()){
            R.parseData(websiteSingleUserService.getUserCount(name));
        } else {
            R.parseData(websiteSingleUserService.getUserCount(null));
        }
        return R;
    }


    @GetMapping("getUserCount")
    public Object getUserCount() {
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleUserService.getUserCount(null));
    }


    @GetMapping("updateUserPermission")
    public Object updateUserPermission(@RequestParam("targetId")String targetId,
                                       @RequestParam("targetPermission")Integer targetPermission,
                                       HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleUserService.updateUserPermission(token, targetId, targetPermission);
        return DataFactory.success(SimpleData.class, "修改成功");
    }


    /**
     * 删除一用户
     * @param id 网站留言ID
     */
    @GetMapping("/deleteUser")
    public Object deleteUser(@RequestParam("id") String id,
                             HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleUserService.deleteUser(id, token);
        return DataFactory.success(SimpleData.class,"删除成功");
    }


    /**
     * 修改用户头像
     * @param file 文件流
     */
    @PostMapping(value = "/update/avatar")
    public Object updateAvatar(@RequestBody MultipartFile file,
                              HttpServletRequest request) throws Exception {
        if (file.getSize() > FileSizeConstants.AVATAR_MAX_SIZE) {
            throw new FileSizeExceededException("上传的文件大小限制为" + FileSizeConstants.AVATAR_MAX_SIZE_NAME);
        }
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "上传成功")
                .parseData(websiteSingleUserService.updateAvatar(file, token));
    }
}
