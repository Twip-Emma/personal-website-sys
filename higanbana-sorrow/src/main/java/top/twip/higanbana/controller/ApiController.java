package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.twip.common.entity.image.ImageSetu;
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

    @GetMapping("/setu")
    public void getSetu(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/jpg");
        List<ImageSetu> setus = apiService.getOneSetu();

        ImageSetu imageSetu = setus.get(0);

        ServletOutputStream outputStream = response.getOutputStream();

//        FileInputStream s = new FileInputStream(Arrays.toString(setu.getContent()));
//        outputStream.write(s.readAllBytes());
        outputStream.write(imageSetu.getContent());
        outputStream.close();
//        s.close();
    }
}
