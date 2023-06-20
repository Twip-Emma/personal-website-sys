package top.twip.higanbana.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.twip.common.constant.CurrencyConstants;
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
        return DataFactory.success(SimpleData.class, "上传成功")
                .parseData(fileService.upload(file, Constant.FILE, null));
    }

    @PostMapping(value = "/upload/image")
    public Object ImageUpload(@RequestBody MultipartFile file,
                              HttpServletRequest request) throws Exception {
        String token = request.getHeader(CurrencyConstants.CURRENCY_HEADER_NAME.getValue());
        return DataFactory.success(SimpleData.class, "上传成功")
                .parseData(fileService.upload(file, Constant.IMAGE, token));
    }
}
