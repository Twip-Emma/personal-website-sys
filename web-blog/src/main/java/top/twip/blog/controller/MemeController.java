package top.twip.blog.controller;

import org.springframework.web.bind.annotation.*;
import top.twip.api.constant.CurrencyConstants;
import top.twip.api.entity.meme.MemeReply;
import top.twip.api.response.DataFactory;
import top.twip.api.response.SimpleData;
import top.twip.blog.service.meme.MemeInfoService;
import top.twip.blog.service.meme.MemeReplyService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/blog/meme")
public class MemeController {
    @Resource
    private MemeInfoService memeInfoService;

    @Resource
    private MemeReplyService memeReplyService;

    @GetMapping("query")
    public Object query(){
        return DataFactory.success(SimpleData.class,"查询成功")
                .parseData(memeInfoService.query());
    }

    @GetMapping("queryLike")
    public Object queryLike(@RequestParam("memeId") String memeId,
                            HttpServletRequest request) {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "操作成功")
                .parseData(memeInfoService.queryLike(memeId, token));
    }

    @GetMapping("updateLike")
    public Object updateLike(@RequestParam("memeId") String memeId,
                             @RequestParam("type") String type,
                             HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        memeInfoService.updateLike(memeId, type, token);
        return DataFactory.success(SimpleData.class, "操作成功");
    }

    @GetMapping("queryReply")
    public Object queryReply(@RequestParam("memeId") String memeId) throws Exception {
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(memeReplyService.query(memeId));
    }


    @PostMapping("insertReply")
    public Object insertReply(@RequestBody MemeReply memeReply,
                             HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        memeReplyService.insert(memeReply.getMemeId(), memeReply.getContent(), token);
        return DataFactory.success(SimpleData.class, "操作成功");
    }
}
