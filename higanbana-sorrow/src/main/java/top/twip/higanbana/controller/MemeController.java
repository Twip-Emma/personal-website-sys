package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.meme.MemeInfoService;

import javax.annotation.Resource;

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
}
