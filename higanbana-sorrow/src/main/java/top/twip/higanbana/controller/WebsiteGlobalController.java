package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.common.constant.CurrencyConstants;
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

    /**
     * 获取网站所有留言
     * @return Object 留言列表
     */
    @GetMapping("/getallmessage")
    public Object getAllMessage() throws Exception{
        return DataFactory.success(SimpleData.class,"查询成功")
                .parseData(websiteCommonService.getAllMessage());
    }

    /**
     * 新增一个网站留言
     * @param websiteMessageEntity 网站留言实体
     */
    @PostMapping("/addmessage")
    public Object addMessage(@RequestBody WebsiteMessageEntity websiteMessageEntity,
                             HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class,"查询成功")
                .parseData(websiteCommonService.addMessage(websiteMessageEntity, token));
    }
}
