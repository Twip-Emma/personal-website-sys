package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/selectbloglistbypage")
    public Object getBlogListByPage(@RequestParam("page")Integer page,
                                       HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListByPage(page));
    }

    @GetMapping("/selectbloglistbyname")
    public Object getBlogListByName(@RequestParam("page")Integer page,
                                    @RequestParam("name")String name,
                                    HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListByName(page, name));
    }

    @GetMapping("/selectbloglisttotalcountbyname")
    public Object getBlogTotalCount(@RequestParam("name")String name,
                                    HttpServletRequest request){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListCountByName(name));
    }

    @GetMapping("/selectbloglisttotalcount")
    public Object getBlogTotalCount(HttpServletRequest request){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogTotalCount());
    }

    @GetMapping("/selectblogreplybyid")
    public Object getBlogReplyById(@RequestParam("blogid")String blogId,
                                   HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getReplyListById(blogId));
    }

    @PostMapping("/addblogreply")
    public Object addBlogReply(@RequestBody WebsiteBlogReplyEntity websiteBlogReplyEntity,
                               HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "新增成功")
                .parseData(websiteSingleBlogService.addReply(websiteBlogReplyEntity));
    }

    @GetMapping("/getbloginfobyid")
    public Object getBlogInfoById(@RequestParam("blogid")String blogId,
                                  HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogById(blogId));
    }
}
