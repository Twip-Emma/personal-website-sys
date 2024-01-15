package top.twip.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.twip.api.enums.CodeEnum;
import top.twip.api.response.DataFactory;
import top.twip.api.response.SimpleData;
import top.twip.blog.service.WebImageService;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/blog/image")
public class WebImageController {

    @Resource
    private WebImageService webImageService;

    /**
     * 根据类型随机获取一张图片
     * @param type 类型
     * @return 图片URL
     */
    @GetMapping("/getRandomImageByType")
    public Object getRandomImageByType(@RequestParam("type")String type) {
        String image = webImageService.getRandomImageByType(type);
        if (image == null) {
            return DataFactory.fail(CodeEnum.NOT_ALL_OK, "该类型的图片为空");
        } else {
            return Objects.requireNonNull(DataFactory.success(SimpleData.class, "获取成功"))
                    .parseData(webImageService.getRandomImageByType(type));
        }
    }
}
