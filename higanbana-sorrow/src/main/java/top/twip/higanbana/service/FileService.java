package top.twip.higanbana.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.twip.common.entity.file.BaseVO;
import top.twip.common.entity.file.Constant;
import top.twip.common.entity.file.FileVO;
import top.twip.common.entity.user.WebsiteUserInfo;
import top.twip.common.exception.BadRequestDataException;
import top.twip.common.util.QiNiuUtil;
import top.twip.common.util.TokenRedisHandler;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.io.FileInputStream;

@Service
public class FileService {

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    public BaseVO upload(MultipartFile file, String fileType, String token) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BadRequestDataException("传入的文件名不能为空");
        }
        if (!this.validateFileName(fileName)) {
            throw new BadRequestDataException("文件名应仅包含汉字、字母、数字、下划线和点号");
        }
        FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
        String url = "";
        if (fileType.equals(Constant.IMAGE)) {
            url = new QiNiuUtil().upload(fileInputStream, Constant.IMAGE);
        } else if (fileType.equals(Constant.FILE)) {
            url = new QiNiuUtil().upload(fileInputStream, Constant.FILE);
        }
        FileVO fileVO = new FileVO();
        fileVO.setDownloadUrl(url);

        // 头像写入用户数据库
        String userId = tokenRedisHandler.getIdByToken(token);
        WebsiteUserInfo user = new WebsiteUserInfo();
        user.setId(userId);
        user.setAvatar(url);
        websiteUserInfoDao.updateById(user);

        return fileVO;
    }
    /**
     * 验证文件名称：仅包含 汉字、字母、数字、下划线和点号
     *
     * @param fileName 文件名称
     * @return 返回true表示符合要求
     */
    private boolean validateFileName(String fileName) {
        String regex = "^[a-zA-Z0-9_\\u4e00-\\u9fa5_\\.]+$";
        return fileName.matches(regex);
    }
}
