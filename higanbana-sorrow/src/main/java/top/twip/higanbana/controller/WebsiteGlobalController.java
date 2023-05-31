package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.entity.blog.WebsiteMessageEntity;
import top.twip.common.response.BaseData;
import top.twip.common.response.DataFactory;
import top.twip.common.response.ListData;
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
    public Object getAllMessage(@RequestParam("page")Integer page,
                                @RequestParam(value = "text", required = false)String text) throws Exception{
        BaseData R = DataFactory.success(ListData.class, "查询成功")
                .parseData(websiteCommonService.getAllMessage(page, text));
        if (text != null && !text.isEmpty()){
            R.parseData(websiteCommonService.getReplyCount(text));
        } else {
            R.parseData(websiteCommonService.getReplyCount(null));
        }
        return R;
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


    /**
     * 修改一个网站留言
     * @param websiteMessageEntity 网站留言实体
     */
    @PostMapping("/updateMessage")
    public Object updateMessage(@RequestBody WebsiteMessageEntity websiteMessageEntity,
                             HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteCommonService.updateMessage(websiteMessageEntity, token);
        return DataFactory.success(SimpleData.class,"查询成功");
    }


    /**
     * 删除一个网站留言
     * @param id 网站留言ID
     */
    @GetMapping("/deleteMessage")
    public Object deleteMessage(@RequestParam("id") String id,
                                HttpServletRequest request) throws Exception{
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        websiteCommonService.deleteMessage(id, token);
        return DataFactory.success(SimpleData.class,"删除成功");
    }
}
