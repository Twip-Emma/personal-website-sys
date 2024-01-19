package top.twip.blog.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.twip.api.constant.CurrencyConstants;
import top.twip.api.entity.blog.WebsiteBlogList;
import top.twip.api.entity.file.Constant;
import top.twip.api.response.DataFactory;
import top.twip.api.response.SimpleData;
import top.twip.blog.service.FileService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/blog/file")
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping(value = "/upload/file")
    public Object fileUpload(MultipartFile file) throws Exception {
//        return DataFactory.success(SimpleData.class, "上传成功")
//                .parseData(fileService.upload(file, Constant.FILE, null));
        return null;
    }

    /**
     * 上传头像专用接口
     * @param file 文件流
     */
    @PostMapping(value = "/upload/image")
    public Object imageUpload(@RequestBody MultipartFile file,
                              HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return null;
//        return DataFactory.success(SimpleData.class, "上传成功")
//                .parseData(fileService.upload(file, Constant.IMAGE, token));
    }

    /**
     * 上传博客配图
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload/uploadBlog")
    public Object uploadBlog(
            @RequestPart("file") MultipartFile file,
            @RequestPart("blog") WebsiteBlogList blog,
            HttpServletRequest request
    ) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "上传成功")
                .parseData(fileService.uploadBlog(
                        file,
                        Constant.IMAGE,
                        blog,
                        token
                ));
    }


    /**
     * 上传素材图片
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload/meme")
    public Object uploadMeme(
            @RequestPart("file") MultipartFile file,
            @RequestPart("title") String title,
            HttpServletRequest request
    ) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return Objects.requireNonNull(DataFactory.success(SimpleData.class, "上传成功"))
                .parseData(fileService.uploadMeme(
                        file,
                        token,
                        title
                ));
    }
}
