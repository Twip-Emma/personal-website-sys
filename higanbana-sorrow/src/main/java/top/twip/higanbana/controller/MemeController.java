package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.meme.MemeInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/higanbana/meme")
public class MemeController {
    @Resource
    private MemeInfoService memeInfoService;

    @GetMapping("/query")
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
}
