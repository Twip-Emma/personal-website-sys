package top.twip.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.twip.api.response.DataFactory;
import top.twip.api.response.SimpleData;
import top.twip.blog.service.WebOverviewService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog/admin")
public class WebOverviewController {

    @Resource
    private WebOverviewService webOverviewService;

    /**
     * @return
     */
    @GetMapping(value = "/getWebOverview")
    public Object getWebOverview() {
        return DataFactory.success(SimpleData.class, "查询成功")
                .parseData(webOverviewService.getWebOverview());
    }

}
