package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.twip.common.constant.CurrencyConstants;
import top.twip.common.entity.blog.WebsiteBlogList;
import top.twip.common.entity.file.BaseVO;
import top.twip.common.entity.file.Constant;
import top.twip.common.response.DataFactory;
import top.twip.common.response.SimpleData;
import top.twip.higanbana.service.FileService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/higanbana/file")
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
}
