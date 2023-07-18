package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.constant.FileSizeConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.common.entity.blog.WebsiteBlogReplyEntity;
import top.twip.common.entity.blog.WebsiteMessageEntity;
import top.twip.common.exception.FileSizeExceededException;
import top.twip.common.response.BaseData;
import top.twip.common.response.DataFactory;
import top.twip.common.response.ListData;
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
     * 分页查询博客列表（管理员界面）
     * @param page 当前页码
     * @return Object 符合条件的结果
     */
    @GetMapping("/queryAllBlog")
    public Object queryAllBlog(@RequestParam("page")Integer page,
                               @RequestParam(value = "name", required = false)String name){
        BaseData R = DataFactory.success(ListData.class, "查询成功")
                .parseData(websiteSingleBlogService.queryAllBlog(page, name));
        if (name != null && !name.isEmpty()){
            R.parseData(websiteSingleBlogService.getBlogListCountByName(name));
        } else {
            R.parseData(websiteSingleBlogService.getBlogTotalCount());
        }
        return R;
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
    public Object getBlogReplyById(@RequestParam("blogid")String blogId,
                                   @RequestParam("page")Integer page,
                                   @RequestParam(value = "text", required = false)String text){
        return DataFactory.success(ListData.class, "查询成功")
                .parseData(websiteSingleBlogService.getReplyListById(blogId, page, text))
                .parseData(websiteSingleBlogService.getBlogReplyCount(blogId, text));
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

    /**
     * 新增博客
     * @param blog 博客实体
     */
    @PostMapping("/add")
    public Object addBlog(@RequestPart(value = "file", required = false) MultipartFile file,
                          @RequestPart("blog") WebsiteBlogList blog,
                          HttpServletRequest request) throws Exception {
        if (file != null && file.getSize() > FileSizeConstants.BLOG_IMAGE_MAX_SIZE) {
            throw new FileSizeExceededException("上传的文件大小限制为" + FileSizeConstants.BLOG_IMAGE_MAX_SIZE_NAME);
        }
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "添加成功")
                .parseData(websiteSingleBlogService.addBlog(file, blog, token));
    }

    /**
     * 获取个人博客分区数据（饼图用）
     */
    @GetMapping("getBlogCountData")
    public Object getBlogCountData(HttpServletRequest request) {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogCountsByUserId(token));
    }


    /**
     * 获取某个人的所有博客信息
     */
    @GetMapping("queryBlogListByUser")
    public Object queryBlogListByUser(HttpServletRequest request) {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.queryBlogListByUser(token));
    }


    /**
     * 根据博客ID修改博客（用户）
     */
    @PostMapping("updateBlogByUser")
    public Object updateBlogByUser(@RequestBody WebsiteBlogList blog,
                                   HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleBlogService.updateBlogByUser(blog, token);
        return DataFactory.success(SimpleData.class, "查询成功");
    }

    /**
     * 删除一个博客（管理员）
     * @param id 网站留言ID
     */
    @GetMapping("/deleteBlog")
    public Object deleteBlog(@RequestParam("id") String id,
                                HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleBlogService.deleteBlog(id, token);
        return DataFactory.success(SimpleData.class,"删除成功");
    }


    /**
     * 删除一个博客（用户）
     * @param id 网站留言ID
     */
    @GetMapping("/deleteBlogByUser")
    public Object deleteBlogByUser(@RequestParam("id") String id,
                             HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleBlogService.deleteBlogByUser(id, token);
        return DataFactory.success(SimpleData.class,"删除成功");
    }


    /**
     * 删除一个博客（管理员操作）
     * @param blogId 博客ID
     */
    @GetMapping("deleteBlogReplyByAdmin")
    public Object deleteBlogReplyByAdmin(@RequestParam("id")String blogId,
                                         HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleBlogService.deleteBlogReplyByAdmin(blogId, token);
        return DataFactory.success(SimpleData.class,"删除成功");
    }


    /**
     * 删除一个博客（用户操作）
     * @param blogId 博客ID
     */
    @GetMapping("deleteBlogReplyByUser")
    public Object deleteBlogReplyByUser(@RequestParam("id")String blogId,
                                         HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleBlogService.deleteBlogReplyByUser(blogId, token);
        return DataFactory.success(SimpleData.class,"删除成功");
    }


    /**
     * 修改一个网站留言
     * @param input 博客回复实体
     */
    @PostMapping("/updateBlogReply")
    public Object updateBlogReply(@RequestBody WebsiteBlogReplyEntity input,
                                HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteSingleBlogService.updateBlogReply(input, token);
        return DataFactory.success(SimpleData.class,"修改成功");
    }
}

