package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.twip.common.entity.image.ImageSetu;
import top.twip.common.enums.CodeEnum;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.ApiService;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-05 22:18
 */
@RestController
@RequestMapping("/higanbana/api")
public class ApiController {
    @Resource
    private ApiService apiService;

    /**
     * 根据KEY获取图片
     * @param key KEY
     * @param response 图片
     */
    @GetMapping("/setu")
    public Object getSetu(@RequestParam("key")String key,
                          HttpServletResponse response) {
        try {
            Boolean aBoolean = apiService.checkKey1(key);
            if (aBoolean){
                response.setHeader("Content-Type", "image/jpg");
                List<ImageSetu> setus = apiService.getOneSetu();
                ImageSetu imageSetu = setus.get(0);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(imageSetu.getContent());
                outputStream.close();
                return DataFactory.success(SimpleData.class, "ok");
            }else {
                return DataFactory.fail(CodeEnum.KEY_FORBIDDEN, "这个key过期了或者没次数了");
            }
        } catch (Exception e){
            return DataFactory.fail(CodeEnum.KEY_FORBIDDEN, "这个key过期了或者没次数了");
        }
    }

    /**
     * 根据KEY获取X级图片
     * @param key KEY
     * @param response 图片
     */
    @GetMapping("/setux")
    public Object getSetux(@RequestParam("key")String key,
                          HttpServletResponse response) throws Exception {
        try {
            Boolean aBoolean = apiService.checkKey1(key);
            if (aBoolean){
                response.setHeader("Content-Type", "image/jpg");
                List<ImageSetu> setus = apiService.getOneSetuX();
                ImageSetu imageSetu = setus.get(0);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(imageSetu.getContent());
                outputStream.close();
                return DataFactory.success(SimpleData.class, "ok");
            }else {
                return DataFactory.fail(CodeEnum.KEY_FORBIDDEN, "这个key过期了或者没次数了");
            }
        } catch (Exception e){
            return DataFactory.fail(CodeEnum.KEY_FORBIDDEN, "这个key过期了或者没次数了");
        }
    }

    /**
     * 获取涩图KEY
     * @param time KEY使用次数
     * @return Object Object
     */
    @GetMapping("/addsetukey")
    public Object addSetuKey(@RequestParam(name = "time", required = false)Integer time){
        return DataFactory.success(SimpleData.class, "ok")
                .parseData(apiService.addKey(time));
//        System.out.println(time);
//        return null;
    }

    /**
     * 删除一个KEY
     * @param key KEY
     */
    @GetMapping("/deletesetukey")
    public Object deleteSetuKey(@RequestParam("key")String key){
        return DataFactory.success(SimpleData.class, "ok")
                .parseData(apiService.deleteKey(key));
    }

    /**
     * 验证KEY是否合法
     * @param key KEY
     */
    @GetMapping("/checksetukey")
    public Object checkSetuKey(@RequestParam("key")String key){
        try {
            Boolean aBoolean = apiService.checkKey2(key);
            if (aBoolean){
                return DataFactory.success(SimpleData.class, "ok");
            }else {
                return DataFactory.fail(CodeEnum.KEY_FORBIDDEN, "这个key过期了或者没次数了");
            }
        } catch (Exception e){
            return DataFactory.fail(CodeEnum.KEY_FORBIDDEN, "这个key过期了或者没次数了");
        }
    }
}
