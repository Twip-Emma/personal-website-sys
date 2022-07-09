package top.twip.higanbana.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.twip.common.constant.PageConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.higanbana.dao.WebsiteBlogListDao;

import javax.annotation.Resource;
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

    // 获取博客列表、分页查询
    public List<WebsiteBlogList> getBlogListByPage(Integer page){
        Page<WebsiteBlogList> objectPage = new Page<>(page, PageConstants.BlogListPageTotal);
        return websiteBlogListDao.selectPage(objectPage, null).getRecords();
    }

    // 获取当前博客数量
    public Integer getBlogTotalCount(){
        return websiteBlogListDao.selectCount(null);
    }
}
