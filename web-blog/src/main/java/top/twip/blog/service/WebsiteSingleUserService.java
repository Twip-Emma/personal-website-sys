package top.twip.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.twip.api.constant.PageConstants;
import top.twip.api.entity.file.FileVO;
import top.twip.api.entity.user.WebsiteAvatarEntity;
import top.twip.api.entity.user.WebsiteUserInfo;
import top.twip.api.enums.FileTypeEnum;
import top.twip.api.exception.BadRequestDataException;
import top.twip.api.exception.DatabaseDataNotFound;
import top.twip.api.exception.DatabaseHandlerException;
import top.twip.api.util.BCryptHandler;
import top.twip.api.util.TokenRedisHandler;
import top.twip.blog.dao.WebsiteAvatarDao;
import top.twip.blog.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:24
 */
@Service
public class WebsiteSingleUserService {

    private final Logger logger = LoggerFactory.getLogger(WebsiteSingleUserService.class);

    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    @Resource
    private WebsiteAvatarDao websiteAvatarDao;

    @Resource
    private BCryptHandler bCryptHandler;

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    @Resource
    private FileService fileService;


    /**
     * 直接查询用户信息
     * @param token TOKEN
     * @return WebsiteUserInfo 用户信息
     */
    public WebsiteUserInfo getUserInfo(String token) {
        String userId = tokenRedisHandler.getIdByToken(token);
        return websiteUserInfoDao.selectById(userId);
    }

    /**
     * 用户登录
     *
     * @param card 账号
     * @param pass 密码
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo userLogin(String card, String pass) throws Exception {
        WebsiteUserInfo one = websiteUserInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if (one == null) {
            throw new BadRequestDataException("用户不存在，请注册");
        }

        // 判断用户密码是否正确
        Boolean a = bCryptHandler.ciphertextToPlaintext(pass, one.getPass());
        if (!a) {
            logger.info("用户登录-失败-密码错误[账号={}]", card);
            throw new BadRequestDataException("密码错误，请重试");
        } else {
            one.setToken(tokenRedisHandler.getToken(one));
            one.setPass(null);
            logger.info("用户登录-成功[账号={},昵称={}]", one.getCard(), one.getNickname());
            return one;
        }
    }

    /**
     * 用户注册
     *
     * @param nickname 昵称
     * @param card     账户
     * @param pass     密码
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo userRegister(String nickname, String card, String pass) throws Exception {
        WebsiteUserInfo one = websiteUserInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if (one != null) {
            logger.info("用户注册-失败-反复注册[账号={}]", card);
            throw new BadRequestDataException("这个账号已经被注册了，请重试");
        }

        String bpass = bCryptHandler.plaintextToCiphertext(pass);
        WebsiteUserInfo user = new WebsiteUserInfo();
        user.setCard(card);
        user.setPass(bpass);
        user.setNickname(nickname);
        user.setIsadmin(0);

        int i = websiteUserInfoDao.insert(user);
        if (i != 1) {
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误");
        }

        one = websiteUserInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if (one == null) {
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误");
        }
        one.setToken(tokenRedisHandler.getToken(one));
        one.setPass(null);
        logger.info("用户注册-成功[账号={},昵称={}]", one.getCard(), one.getNickname());
        return one;
    }

    /**
     * 修改用户信息
     *
     * @param user  用户实体
     * @param token TOKEN
     * @return WebsiteUserInfo 用户实体
     */
    public void updateUser(WebsiteUserInfo user, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);
        user.setId(userId);

        WebsiteUserInfo byId = websiteUserInfoDao.selectById(user.getId());
        if (byId == null) {
            throw new DatabaseDataNotFound("数据未找到");
        }
        // 参数校验
        String newNickName = user.getNickname();
        if (StringUtils.isBlank(newNickName)) {
            user.setNickname(null);
        }
        String newPass = user.getPass();
        if (StringUtils.isNotBlank(newPass)) {
            user.setPass(bCryptHandler.plaintextToCiphertext(newPass));
        }
        String avatar = user.getAvatar();
        if (StringUtils.isBlank(avatar)) {
            user.setAvatar(null);
        } else {
            String avatarUrl = "http://q1.qlogo.cn/g?b=qq&nk=" +
                    avatar +
                    "&s=640";
            user.setAvatar(avatarUrl);
        }
        int i = websiteUserInfoDao.updateById(user);
        if (i != 1) {
            throw new DatabaseHandlerException("数据库执行修改操作失败了");
        }
    }

    /**
     * 修改用户信息（管理员界面）
     *
     * @param user  用户实体
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo updateAllUser(WebsiteUserInfo user) throws Exception {
        WebsiteUserInfo byId = websiteUserInfoDao.selectById(user.getId());
        if (byId == null) {
            throw new DatabaseDataNotFound("数据未找到");
        }
        int i = websiteUserInfoDao.updateById(user);
        if (i != 1) {
            throw new DatabaseHandlerException("数据库执行修改操作失败了");
        }
        byId = websiteUserInfoDao.selectById(user.getId());
        byId.setPass(null);
        return byId;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return Boolean 是否删除成功
     */
    public Boolean deleteUser(String id) {
        int i = websiteUserInfoDao.delete(new QueryWrapper<WebsiteUserInfo>()
                .eq("id", id));
        if (i != 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询头像列表
     *
     * @return List<WebsiteAvatarEntity> 头像实体列表
     */
    public List<WebsiteAvatarEntity> getAllAvatar() throws Exception {
        try {
            return websiteAvatarDao.selectList(null);
        } catch (Exception e) {
            throw new DatabaseHandlerException("数据库查询的时候出错力");
        }
    }

    /**
     * 查询所有用户信息
     *
     * @return List<WebsiteUserInfo> 用户信息实体
     */
    public List<WebsiteUserInfo> getAllUser(Integer page, String name) {
        Page<WebsiteUserInfo> objectPage = new Page<>(page, PageConstants.UserListPageTotal);
        QueryWrapper<WebsiteUserInfo> queryWrapper = new QueryWrapper<>();

        // 根据名称模糊查询
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("nickname", name);
        }

        List<WebsiteUserInfo> websiteUserInfos = websiteUserInfoDao.selectPage(objectPage, queryWrapper).getRecords();
        List<WebsiteUserInfo> resp = new ArrayList<>();
        for (WebsiteUserInfo o : websiteUserInfos) {
            o.setPass(null);
            resp.add(o);
        }
        return resp;
    }


    /**
     * 查询所有用户数量
     * @param name 昵称
     * @return Integer 数量
     */
    public Integer getUserCount(String name) {
        QueryWrapper<WebsiteUserInfo> queryWrapper = new QueryWrapper<>();
        if (name != null) {
            queryWrapper.like("nickname", name);
        }
        return websiteUserInfoDao.selectCount(queryWrapper);
    }

    /**
     * 根据token查询这个用户实体
     *
     * @param token TOKEN
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo getUserByToken(String token) throws Exception {
        if (!tokenRedisHandler.validateToken(token)) {
            throw new BadRequestDataException("验证失败，token已过期！");
        }
        String userId = tokenRedisHandler.getIdByToken(token);
        if (!tokenRedisHandler.validateToken(token)) {
            throw new DatabaseDataNotFound("查不到用户信息");
        }
        WebsiteUserInfo user = websiteUserInfoDao.selectById(userId);
        user.setPass(null);
        return user;
    }


    public void updateUserPermission(String token, String targetId, Integer targetPermission) throws Exception {
        if(!tokenRedisHandler.isSuper(token)){
            throw new BadRequestDataException("权限不足，需要超级管理员权限");
        }
        WebsiteUserInfo user = new WebsiteUserInfo();
        user.setId(targetId);
        user.setIsadmin(targetPermission);
        websiteUserInfoDao.updateById(user);
    }


    /**
     * 删除一个用户
     * @param id 博客ID
     * @param token TOKEN
     */
    public void deleteUser(String id, String token) throws Exception {
        if(!tokenRedisHandler.isAdmin(token)) {
            throw new BadRequestDataException("权限不足，需要管理员权限");
        }
        websiteUserInfoDao.deleteById(id);
    }

    /**
     * 更新用户头像
     * @param file 文件
     * @param token TOKEN
     * @return FileVO VO
     */
    public FileVO updateAvatar(MultipartFile file, String token) throws Exception {
        // 上传图片，获取url
        FileVO vo = fileService.upload(file, FileTypeEnum.IMAGE);

        // 根据url更新进数据库
        String userId = tokenRedisHandler.getIdByToken(token);
        WebsiteUserInfo user = new WebsiteUserInfo();
        user.setId(userId);
        user.setAvatar(vo.getDownloadUrl());
        websiteUserInfoDao.updateById(user);
        return vo;
    }
}
