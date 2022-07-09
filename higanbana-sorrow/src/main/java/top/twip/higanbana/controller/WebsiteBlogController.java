package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
//        Thread.sleep(1*1000);
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogListByPage(page));
    }

    @GetMapping("/selectbloglisttotalcount")
    public Object getBlogTotalCount(HttpServletRequest request){
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(websiteSingleBlogService.getBlogTotalCount());
    }
}
