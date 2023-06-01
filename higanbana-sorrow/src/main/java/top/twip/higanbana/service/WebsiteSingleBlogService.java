package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.val;
import org.springframework.stereotype.Service;
import top.twip.common.constant.PageConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.common.entity.blog.WebsiteBlogReplyEntity;
import top.twip.common.entity.blog.WebsiteMessageEntity;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.BadRequestDataException;
import top.twip.common.exception.DatabaseDataNotFound;
import top.twip.common.exception.DatabaseHandlerException;
import top.twip.common.util.TokenRedisHandler;
import top.twip.higanbana.dao.WebsiteBlogListDao;
import top.twip.higanbana.dao.WebsiteBlogReplyEntityDao;
import top.twip.higanbana.dao.WebsiteMessageEntityDao;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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


    /**
     * 获取博客列表、分页查询
     *
     * @param page 当前页码
     * @return List<WebsiteBlogList> 博客列表
     */
    public List<WebsiteBlogList> getBlogListByPage(Integer page) {
        Page<WebsiteBlogList> objectPage = new Page<>(page, PageConstants.BlogListPageTotal);
        LambdaQueryWrapper<WebsiteBlogList> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(WebsiteBlogList::getCtime);
        List<WebsiteBlogList> records = websiteBlogListDao.selectPage(objectPage, wrapper).getRecords();
        return getWebsiteBlogLists(records);
    }


    /**
     * 获取博客列表、分页查询（管理员界面）
     *
     * @param page 当前页码
     * @return List<WebsiteBlogList> 博客列表
     */
    public List<WebsiteBlogList> queryAllBlog(Integer page, String name) {
        Page<WebsiteBlogList> objectPage = new Page<>(page, 10);
        LambdaQueryWrapper<WebsiteBlogList> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(WebsiteBlogList::getCtime);

        // 根据名称模糊查询
        if (name != null && !name.isEmpty()) {
            wrapper.like(WebsiteBlogList::getTitle, name);
        }

        List<WebsiteBlogList> records = websiteBlogListDao.selectPage(objectPage, wrapper).getRecords();
        return getWebsiteBlogLists(records);
    }


    /**
     * 获取博客列表、分页+模糊查询
     *
     * @param page 当前页码
     * @param name 名称
     * @return List<WebsiteBlogList> 博客列表
     */
    public List<WebsiteBlogList> getBlogListByName(Integer page, String name) {
        Integer startNum = PageConstants.BlogListPageTotal * (page - 1);
        List<WebsiteBlogList> records = websiteBlogListDao
                .getBlogByName("%" + name + "%", startNum, PageConstants.BlogListPageTotal);
        return getWebsiteBlogLists(records);
    }


    /**
     * 获取博客列表、分页+模糊查询（查询数量）
     * @param name 名称
     * @return Integer 数量
     */
    public Integer getBlogListCountByName(String name) {
        return websiteBlogListDao.getBlogCountByName("%" + name + "%");
    }

    /**
     * 获取当前博客数量
     * @return Integer 数量
     */
    public Integer getBlogTotalCount() {
        return websiteBlogListDao.selectCount(null);
    }

    /**
     * 获取当前博客下的评论
     * @param blogId 博客ID
     * @return List<WebsiteBlogReplyEntity> 评论实体列表
     */
    public List<WebsiteBlogReplyEntity> getReplyListById(String blogId, Integer page, String text) {
        Page<WebsiteBlogReplyEntity> objectPage = new Page<>(page, 10);
        QueryWrapper<WebsiteBlogReplyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", blogId).orderByDesc("ctime");

        if (text != null && !"".equals(text)) {
            wrapper.like("content", text);
        }

        wrapper.orderByDesc("ctime");
        Page<WebsiteBlogReplyEntity> pageResult = websiteBlogReplyEntityDao.selectPage(objectPage, wrapper);
        List<WebsiteBlogReplyEntity> entities = pageResult.getRecords();

        List<WebsiteBlogReplyEntity> resp = new ArrayList<>();
        for (WebsiteBlogReplyEntity o : entities) {
            WebsiteUserInfo userInfo = websiteUserInfoDao.selectById(o.getUserId());
            if (userInfo == null) {
                continue;
            }
            o.setAvatar(userInfo.getAvatar());
            o.setNickname(userInfo.getNickname());
            resp.add(o);
        }
        return resp;
    }


    /**
     * 查询所有网站评论数量
     * @param blogId 博客ID
     * @param text 文本
     * @return Integer 数量
     */
    public Integer getBlogReplyCount(String blogId, String text) {
        QueryWrapper<WebsiteBlogReplyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", blogId).orderByDesc("ctime");
        if (text != null) {
            queryWrapper.like("content", text);
        }
        return websiteBlogReplyEntityDao.selectCount(queryWrapper);
    }


    /**
     * 根据ID获取博客内容
     * @param id 博客ID
     * @return WebsiteBlogList 博客实体
     */
    public WebsiteBlogList getBlogById(String id) throws Exception {
        WebsiteBlogList blog = websiteBlogListDao.selectById(id);
        if (blog == null) {
            throw new DatabaseDataNotFound("数据未找到");
        }
        WebsiteUserInfo websiteUserInfo = websiteUserInfoDao.selectById(blog.getUserId());
        websiteUserInfo.setPass(null);
        blog.setUser(websiteUserInfo);
        blog.setViews(blog.getViews() + 1);
        websiteBlogListDao.updateById(blog);
        return getStringByBytes(blog);
    }

    /**
     * 发表博客评论
     * @param input 博客评论实体
     * @param token TOKEN
     * @return WebsiteBlogReplyEntity 博客评论实体
     */
    public WebsiteBlogReplyEntity addReply(WebsiteBlogReplyEntity input, String token) throws Exception {
        String id = tokenRedisHandler.getIdByToken(token);
        input.setUserId(id);
        int i = websiteBlogReplyEntityDao.insert(input);
        if (i != 1) {
            throw new DatabaseHandlerException("数据库执行插入的时候出现错误力");
        }
        return input;
    }

    /**
     * 新增博客
     * @param blog 博客实体
     * @param token TOKEN
     * @return Boolean 是否成功
     */
    public Boolean addBlog(WebsiteBlogList blog, String token) {
        String id = tokenRedisHandler.getIdByToken(token);
        blog.setUserId(id);
        blog.setViews(0);
        blog.setTypeColor("#59c9fb");
        blog.setContentBytes(blog.getContent().getBytes());
        int i = websiteBlogListDao.insert(blog);
        return i == 1;
    }

    /**
     * <私有方法>将博客列表实体的user属性对应到用户实体
     *
     * @param records 没有用户实体的博客列表
     * @return List<WebsiteBlogList> 有用户实体的博客列表
     */
    private List<WebsiteBlogList> getWebsiteBlogLists(List<WebsiteBlogList> records) {
        // 使用Stream API对传入的records列表进行操作，提取出所有的userId，放入一个列表中
        List<String> userIds = records.stream()
                .map(WebsiteBlogList::getUserId)
                .collect(Collectors.toList());

        // 使用mybatis-plus的selectBatchIds方法，将上一步提取出来的userId列表作为参数，
        // 一次性查询出对应的所有WebsiteUserInfo对象，放入一个列表中。
        List<WebsiteUserInfo> users = websiteUserInfoDao.selectBatchIds(userIds);

        // 将上一步查询出来的WebsiteUserInfo对象列表转化为Map对象，以id为键，对象本身为值，方便后续使用。
        Map<String, WebsiteUserInfo> userMap = users.stream()
                .collect(Collectors.toMap(WebsiteUserInfo::getId, Function.identity()));
        for (WebsiteBlogList o : records) {
            o.setUser(userMap.get(o.getUserId()));
        }
        return records;
    }

    private WebsiteBlogList getStringByBytes(WebsiteBlogList blog) {
        blog.setContent(new String(blog.getContentBytes()));
        return blog;
    }


    /**
     * 获取个人博客分区数据（饼图用）
     * @param token TOKEN
     * @return Map<String, Object> 数据
     */
    public Map<String, Object> getBlogCountsByUserId(String token) {
        String userId = tokenRedisHandler.getIdByToken(token);
        List<Map<String, Object>> data = websiteBlogListDao.getBlogCountsByUserId(userId);
        return data.get(0);
    }


    /**
     * 获取这个用户下的所有博客
     * @param token TOKEN
     * @return List<WebsiteBlogList> 这个用户下的所有博客
     */
    public List<WebsiteBlogList> queryBlogListByUser(String token) {
        String userId = tokenRedisHandler.getIdByToken(token);
        return websiteBlogListDao.selectList(new QueryWrapper<WebsiteBlogList>()
                .eq("user_id", userId)
                .orderByDesc("ctime"));
    }


    public void updateBlogByUser(WebsiteBlogList blog, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);
        WebsiteBlogList re = websiteBlogListDao.selectById(blog.getId());
        if (!userId.equals(re.getUserId())){
            if (!tokenRedisHandler.isAdmin(token)){
                throw new BadRequestDataException("你不能修改其他人的博客");
            }
        }
        websiteBlogListDao.updateById(blog);
    }

    /**
     * 删除一个博客（管理员）
     * @param id 博客ID
     * @param token TOKEN
     */
    public void deleteBlog(String id, String token) throws Exception {
        if(!tokenRedisHandler.isAdmin(token)) {
            throw new BadRequestDataException("权限不足，需要管理员权限");
        }
        websiteBlogListDao.deleteById(id);
    }


    /**
     * 删除一个博客（用户）
     * @param id 博客ID
     * @param token TOKEN
     */
    public void deleteBlogByUser(String id, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);
        WebsiteBlogList re = websiteBlogListDao.selectById(id);
        if (!userId.equals(re.getUserId())){
            if (!tokenRedisHandler.isAdmin(token)){
                throw new BadRequestDataException("你不能删除其他人的博客");
            }
        }
        websiteBlogListDao.deleteById(id);
    }


    /**
     * 删除网站留言（管理员操作）
     *
     * @param id 网站留言ID
     * @param token TOKEN
     * @return WebsiteMessageEntity 网站留言实体
     */
    public void deleteBlogReplyByAdmin(String id, String token) throws Exception {
        if(!tokenRedisHandler.isAdmin(token)) {
            throw new BadRequestDataException("权限不足，需要管理员权限");
        }
        websiteBlogReplyEntityDao.deleteById(id);
    }


    /**
     * 删除网站留言（用户操作）
     *
     * @param id 网站留言ID
     * @param token TOKEN
     * @return WebsiteMessageEntity 网站留言实体
     */
    public void deleteBlogReplyByUser(String id, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);
        WebsiteBlogReplyEntity reply = websiteBlogReplyEntityDao.selectById(id);
        if (!userId.equals(reply.getUserId())) {
            throw new BadRequestDataException("你不能修改修改别人的评论");
        }
        websiteBlogReplyEntityDao.deleteById(id);
    }


    /**
     * 修改博客评论
     *
     * @param input 网站留言实体
     * @param token TOKEN
     * @return WebsiteMessageEntity 网站留言实体
     */
    public void updateBlogReply(WebsiteBlogReplyEntity input, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);
        WebsiteBlogReplyEntity reply = websiteBlogReplyEntityDao.selectById(input.getId());

        if (tokenRedisHandler.isAdmin(token)) {
            // 管理员权限修改
            websiteBlogReplyEntityDao.updateById(input);
        } else if (userId.equals(reply.getUserId())) {
            // 用户修改自己的评论
            websiteBlogReplyEntityDao.updateById(input);
        } else {
            throw new BadRequestDataException("权限不足，无法删除评论");
        }
    }
}
