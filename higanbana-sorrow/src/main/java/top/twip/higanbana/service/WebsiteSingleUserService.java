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
    private TokenHandler tokenHandler;

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
            one.setToken(tokenHandler.getToken(one));
            one.setPass(null);
            return one;
        }
    }


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
        one.setToken(tokenHandler.getToken(one));
        one.setPass(null);
        return one;
    }

    //修改用户
    public WebsiteUserInfo updateUser(WebsiteUserInfo user) throws Exception{
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

    // 删除用户
    public Boolean deleteUser(String id) throws Exception{
        int i = websiteUserInfoDao.delete(new QueryWrapper<WebsiteUserInfo>()
                .eq("id", id));
        if(i != 1){
            return false;
        }else {
            return true;
        }
    }

    // 查询所有头像
    public List<WebsiteAvatarEntity> getAllAvatar() throws Exception{
        try {
            return websiteAvatarDao.selectList(null);
        } catch (Exception e){
            throw new DatabaseHandlerException("数据库查询的时候出错力");
        }
    }

    // 查询所有用户信息
    public List<WebsiteUserInfo> getAllUser() throws Exception{
        List<WebsiteUserInfo> websiteUserInfos = websiteUserInfoDao.selectList(null);
        List<WebsiteUserInfo> resp = new ArrayList<>();
        for(WebsiteUserInfo o: websiteUserInfos){
            o.setPass(null);
            resp.add(o);
        }
        return resp;
    }
}
