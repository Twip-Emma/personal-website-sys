package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.BadRequestDataException;
import top.twip.common.exception.DatabaseHandlerException;
import top.twip.common.util.BCryptHandler;
import top.twip.common.util.TokenHandler;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:24
 */
@Service
public class WebsiteSingleUserService {
    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

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
}
