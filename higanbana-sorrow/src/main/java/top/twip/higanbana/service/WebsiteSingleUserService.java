package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.BadRequestDataException;
import top.twip.common.util.BCryptHandler;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:24
 */
@Service
public class WebsiteSingleUserService {
    @Resource
    private WebsiteUserInfoDao userInfoDao;

    @Resource
    private BCryptHandler bCryptHandler;

    public WebsiteUserInfo userLogin(String card,String pass) throws Exception{
        WebsiteUserInfo one = userInfoDao.selectOne(new QueryWrapper<WebsiteUserInfo>()
                .eq("card", card));
        if (one == null){
            throw new BadRequestDataException("用户不存在，请注册");
        }

        // 判断用户密码是否正确
        Boolean a = bCryptHandler.ciphertextToPlaintext(card, pass);
        if (!a){
            throw new BadRequestDataException("密码错误，请重试");
        }else {
            return one;
        }
    }
}
