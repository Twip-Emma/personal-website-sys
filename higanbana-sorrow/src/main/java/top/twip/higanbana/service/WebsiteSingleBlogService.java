package top.twip.higanbana.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.twip.common.constant.PageConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.higanbana.dao.WebsiteBlogListDao;
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

    // 获取博客列表、分页查询
    public List<WebsiteBlogList> getBlogListByPage(Integer page){
        Page<WebsiteBlogList> objectPage = new Page<>(page, PageConstants.BlogListPageTotal);
        List<WebsiteBlogList> records = websiteBlogListDao.selectPage(objectPage, null).getRecords();
        List<WebsiteBlogList> resp = new ArrayList<>();
        for(WebsiteBlogList o: records){
            WebsiteUserInfo websiteUserInfo = websiteUserInfoDao.selectById(o.getUserId());
            o.setUser(websiteUserInfo);
            resp.add(o);
        }
        return resp;
    }

    // 获取当前博客数量
    public Integer getBlogTotalCount(){
        return websiteBlogListDao.selectCount(null);
    }
}
