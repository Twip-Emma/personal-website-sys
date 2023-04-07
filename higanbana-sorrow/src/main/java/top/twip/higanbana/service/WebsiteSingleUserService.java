package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.common.entity.user.WebsiteAvatarEntity;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.BadRequestDataException;
import top.twip.common.exception.DatabaseDataNotFound;
import top.twip.common.exception.DatabaseHandlerException;
import top.twip.common.util.BCryptHandler;
import top.twip.common.util.TokenHandler;
import top.twip.common.util.TokenRedisHandler;
import top.twip.higanbana.dao.WebsiteAvatarDao;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:24
 */
@Service
public class WebsiteSingleUserService {
    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    @Resource
    private WebsiteAvatarDao websiteAvatarDao;

    @Resource
    private BCryptHandler bCryptHandler;

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    /**
     * 用户登录
     * @param card 账号
     * @param pass 密码
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo userLogin(String card,String pass) throws Exception{
        WebsiteUserInfo one = websiteUserInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if (one == null){
            throw new BadRequestDataException("用户不存在，请注册");
        }

        // 判断用户密码是否正确
        Boolean a = bCryptHandler.ciphertextToPlaintext(pass, one.getPass());
        if (!a){
            throw new BadRequestDataException("密码错误，请重试");
        }else {
            one.setToken(tokenRedisHandler.getToken(one));
            one.setPass(null);
            return one;
        }
    }

    /**
     * 用户注册
     * @param nickname 昵称
     * @param card 账户
     * @param pass 密码
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo userRegister(String nickname,String card,String pass) throws Exception{
        WebsiteUserInfo one = websiteUserInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if (one != null){
            throw new BadRequestDataException("这个账号已经被注册了，请重试");
        }

        String bpass = bCryptHandler.plaintextToCiphertext(pass);
//        String bpass = pass;
        WebsiteUserInfo user = new WebsiteUserInfo();
        user.setCard(card);
        user.setPass(bpass);
        user.setNickname(nickname);
        user.setIsadmin(0);

        int i = websiteUserInfoDao.insert(user);
        if (i != 1){
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误力");
        }

        one = websiteUserInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if(one == null){
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误力");
        }
        one.setToken(tokenRedisHandler.getToken(one));
        one.setPass(null);
        return one;
    }

    /**
     * 修改用户信息
     * @param user 用户实体
     * @param token TOKEN
     * @return WebsiteUserInfo 用户实体
     */
    public WebsiteUserInfo updateUser(WebsiteUserInfo user, String token) throws Exception{
        String userId = tokenRedisHandler.getIdByToken(token);
        user.setId(userId);

        WebsiteUserInfo byId = websiteUserInfoDao.selectById(user.getId());
        if (byId == null){
            throw new DatabaseDataNotFound("数据未找到");
        }
        int i = websiteUserInfoDao.updateById(user);
        if (i != 1){
            throw new DatabaseHandlerException("数据库执行修改操作失败了");
        }
        byId = websiteUserInfoDao.selectById(user.getId());
        byId.setPass(null);
        return byId;
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return Boolean 是否删除成功
     */
    public Boolean deleteUser(String id){
        int i = websiteUserInfoDao.delete(new QueryWrapper<WebsiteUserInfo>()
                .eq("id", id));
        if(i != 1){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 查询头像列表
     * @return List<WebsiteAvatarEntity> 头像实体列表
     */
    public List<WebsiteAvatarEntity> getAllAvatar() throws Exception{
        try {
            return websiteAvatarDao.selectList(null);
        } catch (Exception e){
            throw new DatabaseHandlerException("数据库查询的时候出错力");
        }
    }

    /**
     * 查询所有用户信息
     * @return List<WebsiteUserInfo> 用户信息实体
     */
    public List<WebsiteUserInfo> getAllUser(){
        List<WebsiteUserInfo> websiteUserInfos = websiteUserInfoDao.selectList(null);
        List<WebsiteUserInfo> resp = new ArrayList<>();
        for(WebsiteUserInfo o: websiteUserInfos){
            o.setPass(null);
            resp.add(o);
        }
        return resp;
    }
}
