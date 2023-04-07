package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.common.entity.blog.WebsiteBlogReplyEntity;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.WebsiteSingleBlogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:29
 */
@RestController
@RequestMapping("/higanbana/blog/blog")
public class WebsiteBlogController {

    @Resource
    private WebsiteSingleBlogService websiteSingleBlogService;

    /**
     * 分页查询博客列表
     * @param page 当前页码
     * @return Object 符合条件的结果
     */
    @GetMapping("/selectbloglistbypage")
    public Object getBlogListByPage(@RequestParam("page")Integer page){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListByPage(page));
    }

    /**
     * 分页查询的基础上名称模糊查询博客列表
     * @param page 当前页码
     * @param name 名称
     * @return Object 符合条件的结果
     */
    @GetMapping("/selectbloglistbyname")
    public Object getBlogListByName(@RequestParam("page")Integer page,
                                    @RequestParam("name")String name){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListByName(page, name));
    }

    /**
     * 名称模糊查询博客总数
     * @param name 名称
     * @return Object 符合条件的总数
     */
    @GetMapping("/selectbloglisttotalcountbyname")
    public Object getBlogTotalCount(@RequestParam("name")String name){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListCountByName(name));
    }

    /**
     * 查询博客总数
     * @return Object 总数
     */
    @GetMapping("/selectbloglisttotalcount")
    public Object getBlogTotalCount(){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogTotalCount());
    }

    /**
     * 获取某个博客的评论
     * @param blogId 博客ID
     * @return Object 博客的评论列表
     */
    @GetMapping("/selectblogreplybyid")
    public Object getBlogReplyById(@RequestParam("blogid")String blogId){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getReplyListById(blogId));
    }

    /**
     * 新增博客评论
     * @param websiteBlogReplyEntity 博客评论实体
     */
    @PostMapping("/addblogreply")
    public Object addBlogReply(@RequestBody WebsiteBlogReplyEntity websiteBlogReplyEntity,
                               HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "新增成功")
                .parseData(websiteSingleBlogService.addReply(websiteBlogReplyEntity, token));
    }

    /**
     * 根据ID获取博客详细内容
     * @param blogId 博客ID
     * @return Object 博客实体
     */
    @GetMapping("/getbloginfobyid")
    public Object getBlogInfoById(@RequestParam("blogid")String blogId) throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogById(blogId));
    }

    @PostMapping("/add")
    public Object addBlog(@RequestBody WebsiteBlogList blog,
                          HttpServletRequest request) {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "添加成功")
                .parseData(websiteSingleBlogService.addBlog(blog, token));
    }
}
