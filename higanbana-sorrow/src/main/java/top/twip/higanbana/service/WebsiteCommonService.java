package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.common.entity.blog.WebsiteMessageEntity;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.DatabaseHandlerException;
import top.twip.higanbana.dao.WebsiteMessageEntityDao;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:28
 */
@Service
public class WebsiteCommonService {

    @Resource
    private WebsiteMessageEntityDao websiteMessageEntityDao;

    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    // 获取所有全局评论
    public List<WebsiteMessageEntity> getAllMessage() throws Exception{
        try{
            List<WebsiteMessageEntity> entities = websiteMessageEntityDao.selectList(null);
            for(WebsiteMessageEntity o: entities){
                WebsiteUserInfo userInfo = websiteUserInfoDao.selectById(o.getUserId());
                o.setAvatar(userInfo.getAvatar());
                o.setNickname(userInfo.getNickname());
            }
            return entities;
        } catch (Exception e){
            throw new DatabaseHandlerException(e.getMessage());
        }
    }


    // 发表全局评论
    public WebsiteMessageEntity addMessage(WebsiteMessageEntity input) throws Exception{
        int i = websiteMessageEntityDao.insert(input);
        if(i != 1){
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误力");
        }
        return input;
    }
}
