package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.common.entity.blog.WebsiteMessageEntity;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.WebsiteCommonService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/higanbana/blog/global")
public class WebsiteGlobalController {

    @Resource
    WebsiteCommonService websiteCommonService;

    @GetMapping("/getallmessage")
    public Object getAllMessage(HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class,"查询成功")
                .parseData(websiteCommonService.getAllMessage());
    }

    @PostMapping("/addmessage")
    public Object addMessage(@RequestBody WebsiteMessageEntity websiteMessageEntity,
                             HttpServletRequest request) throws Exception{
        return DataFactory.success(SimpleData.class,"查询成功")
                .parseData(websiteCommonService.addMessage(websiteMessageEntity));
    }
}
