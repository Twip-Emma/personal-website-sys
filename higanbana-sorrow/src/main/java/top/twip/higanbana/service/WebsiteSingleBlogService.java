package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.val;
import org.springframework.stereotype.Service;
import top.twip.common.constant.PageConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.common.entity.blog.WebsiteBlogReplyEntity;
import top.twip.common.entity.blog.WebsiteMessageEntity;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.DatabaseDataNotFound;
import top.twip.common.exception.DatabaseHandlerException;
import top.twip.common.util.TokenRedisHandler;
import top.twip.higanbana.dao.WebsiteBlogListDao;
import top.twip.higanbana.dao.WebsiteBlogReplyEntityDao;
import top.twip.higanbana.dao.WebsiteMessageEntityDao;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:25
 */
@Service
public class WebsiteSingleBlogService {

    @Resource
    private WebsiteBlogListDao websiteBlogListDao;

    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    @Resource
    private WebsiteBlogReplyEntityDao websiteBlogReplyEntityDao;

    @Resource
    private TokenRedisHandler tokenRedisHandler;


    // 获取博客列表、分页查询
    public List<WebsiteBlogList> getBlogListByPage(Integer page){
        Page<WebsiteBlogList> objectPage = new Page<>(page, PageConstants.BlogListPageTotal);
        List<WebsiteBlogList> records = websiteBlogListDao.selectPage(objectPage, null).getRecords();
        return getWebsiteBlogLists(records);
    }

    // 获取博客列表、分页+模糊查询
    public List<WebsiteBlogList> getBlogListByName(Integer page,String name){
        Integer startNum = PageConstants.BlogListPageTotal * (page - 1);
        List<WebsiteBlogList> records = websiteBlogListDao.getBlogByName("%" + name + "%", startNum, PageConstants.BlogListPageTotal);
        return getWebsiteBlogLists(records);
    }

    private List<WebsiteBlogList> getWebsiteBlogLists(List<WebsiteBlogList> records) {
        List<WebsiteBlogList> resp = new ArrayList<>();
        for(WebsiteBlogList o: records){
            WebsiteUserInfo websiteUserInfo = websiteUserInfoDao.selectById(o.getUserId());
            o.setUser(websiteUserInfo);
            resp.add(o);
        }
        return resp;
    }

    // 获取博客列表、分页+模糊查询（查询数量）
    public Integer getBlogListCountByName(String name){
        return websiteBlogListDao.getBlogCountByName("%"+name+"%");
    }

    // 获取当前博客数量
    public Integer getBlogTotalCount(){
        return websiteBlogListDao.selectCount(null);
    }

    // 获取对应博客下的评论列表
    public List<WebsiteBlogReplyEntity> getReplyListById(String blogId){
        List<WebsiteBlogReplyEntity> entities = websiteBlogReplyEntityDao.selectList(new QueryWrapper<WebsiteBlogReplyEntity>()
                .eq("article_id", blogId));

        List<WebsiteBlogReplyEntity> resp = new ArrayList<>();
        for(WebsiteBlogReplyEntity o: entities){
            WebsiteUserInfo userInfo = websiteUserInfoDao.selectById(o.getUserId());
            if (userInfo == null){
                continue;
            }
            o.setAvatar(userInfo.getAvatar());
            o.setNickname(userInfo.getNickname());
            resp.add(o);
        }
        return resp;
    }

    // 根据ID获取博客内容
    public WebsiteBlogList getBlogById(String id) throws Exception{
        WebsiteBlogList blog = websiteBlogListDao.selectById(id);
        if(blog == null){
            throw new DatabaseDataNotFound("数据未找到");
        }
        WebsiteUserInfo websiteUserInfo = websiteUserInfoDao.selectById(blog.getUserId());
        websiteUserInfo.setPass(null);
        blog.setUser(websiteUserInfo);
        blog.setViews(blog.getViews() + 1);
        websiteBlogListDao.updateById(blog);
        return blog;
    }

    // 发表博客评论
    public WebsiteBlogReplyEntity addReply(WebsiteBlogReplyEntity input, String token) throws Exception{

//        WebsiteBlogReplyEntity reply = new WebsiteBlogReplyEntity();
//        reply.setContent(input.getContent());
//        reply.setArticleId(input.getArticleId());
//        reply.setUserId(input.getUserId());
        String id = tokenRedisHandler.getIdByToken(token);
        input.setUserId(id);
        int i = websiteBlogReplyEntityDao.insert(input);
        if(i != 1){
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误力");
        }
        return input;
    }
}
