package top.twip.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.twip.api.entity.blog.WebsiteMessageEntity;
import top.twip.api.entity.user.WebsiteUserInfo;
import top.twip.api.exception.BadRequestDataException;
import top.twip.api.exception.DatabaseHandlerException;
import top.twip.api.util.TokenRedisHandler;
import top.twip.blog.dao.WebsiteMessageEntityDao;
import top.twip.blog.dao.WebsiteUserInfoDao;

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

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    /**
     * 获取所有网站留言
     *
     * @return List<WebsiteMessageEntity> 网站留言实体列表
     */
    public List<WebsiteMessageEntity> getAllMessage(Integer page, String text) throws Exception {
        Page<WebsiteMessageEntity> objectPage = new Page<>(page, 10);
        QueryWrapper<WebsiteMessageEntity> wrapper = new QueryWrapper<>();

        if (text != null && !"".equals(text)) {
            wrapper.like("content", text);
        }

        wrapper.orderByDesc("ctime");
        Page<WebsiteMessageEntity> pageResult = websiteMessageEntityDao.selectPage(objectPage, wrapper);
        List<WebsiteMessageEntity> entities = pageResult.getRecords();

        for (WebsiteMessageEntity o : entities) {
            WebsiteUserInfo userInfo = websiteUserInfoDao.selectById(o.getUserId());
            if (userInfo == null) {
                continue;
            }
            o.setAvatar(userInfo.getAvatar());
            o.setNickname(userInfo.getNickname());
        }

        return entities;
    }


    /**
     * 查询所有网站评论数量
     * @param text 文本
     * @return Integer 数量
     */
    public Integer getReplyCount(String text) {
        QueryWrapper<WebsiteMessageEntity> queryWrapper = new QueryWrapper<>();
        if (text != null) {
            queryWrapper.like("content", text);
        }
        return websiteMessageEntityDao.selectCount(queryWrapper);
    }



    /**
     * 新增网站留言
     *
     * @param input 网站留言实体
     * @param token TOKEN
     * @return WebsiteMessageEntity 网站留言实体
     */
    public WebsiteMessageEntity addMessage(WebsiteMessageEntity input, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);
        input.setUserId(userId);
        int i = websiteMessageEntityDao.insert(input);
        if (i != 1) {
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误力");
        }
        return input;
    }


    /**
     * 修改网站留言
     *
     * @param input 网站留言实体
     * @param token TOKEN
     * @return WebsiteMessageEntity 网站留言实体
     */
    public void updateMessage(WebsiteMessageEntity input, String token) throws Exception {
        if(!tokenRedisHandler.isAdmin(token)) {
            throw new BadRequestDataException("权限不足，需要管理员权限");
        }
        WebsiteMessageEntity message = new WebsiteMessageEntity();
        message.setId(input.getId());
        message.setContent(input.getContent());
        websiteMessageEntityDao.updateById(message);
    }


    /**
     * 删除网站留言
     *
     * @param id 网站留言ID
     * @param token TOKEN
     * @return WebsiteMessageEntity 网站留言实体
     */
    public void deleteMessage(String id, String token) throws Exception {
        if(!tokenRedisHandler.isAdmin(token)) {
            throw new BadRequestDataException("权限不足，需要管理员权限");
        }
        websiteMessageEntityDao.deleteById(id);
    }
}
