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

    /**
     * 查询meme列表
     */
    @GetMapping("query")
    public Object query() {
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(memeInfoService.query());
    }

    /**
     * 查询指定meme获赞情况
     *
     * @param memeId memeId
     */
    @GetMapping("queryLike")
    public Object queryLike(@RequestParam("memeId") String memeId,
                            HttpServletRequest request) {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "操作成功")
                .parseData(memeInfoService.queryLike(memeId, token));
    }

    /**
     * 用户对某个meme赞/取消赞
     *
     * @param memeId memeId
     * @param type   操作类型
     * @throws Exception Exception
     */
    @GetMapping("updateLike")
    public Object updateLike(@RequestParam("memeId") String memeId,
                             @RequestParam("type") String type,
                             HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        memeInfoService.updateLike(memeId, type, token);
        return DataFactory.success(SimpleData.class, "操作成功");
    }

    /**
     * 查询meme评论
     *
     * @param memeId memeId
     * @throws Exception Exception
     */
    @GetMapping("queryReply")
    public Object queryReply(@RequestParam("memeId") String memeId) throws Exception {
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(memeReplyService.query(memeId));
    }

    /**
     * 新增meme评论
     *
     * @param memeReply 评论实体
     * @throws Exception Exception
     */
    @PostMapping("insertReply")
    public Object insertReply(@RequestBody MemeReply memeReply,
                              HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        memeReplyService.insert(memeReply.getMemeId(), memeReply.getContent(), token);
        return DataFactory.success(SimpleData.class, "操作成功");
    }
}
