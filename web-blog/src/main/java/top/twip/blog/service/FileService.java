package top.twip.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.twip.api.entity.blog.WebsiteBlogList;
import top.twip.api.entity.file.BaseVO;
import top.twip.api.entity.file.Constant;
import top.twip.api.entity.file.FileVO;
import top.twip.api.entity.meme.MemeInfo;
import top.twip.api.enums.FileTypeEnum;
import top.twip.api.exception.BadRequestDataException;
import top.twip.api.util.QiNiuUtil;
import top.twip.api.util.TokenRedisHandler;
import top.twip.blog.dao.MemeInfoDao;
import top.twip.blog.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.util.UUID;

@Service
public class FileService {

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    @Resource
    private MemeInfoDao memeInfoDao;

    /**
     * 文件上传
     * @param file 文件
     * @param fileType 文件类型
     * @return BaseVO 文件对象
     */
    public FileVO upload(MultipartFile file, FileTypeEnum fileType) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BadRequestDataException("传入的文件名不能为空");
        }
        if (!this.validateFileName(fileName)) {
            throw new BadRequestDataException("文件名应仅包含汉字、字母、数字、下划线和点号");
        }
        FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
        String url = "";
        if (fileType.equals(FileTypeEnum.IMAGE)) {
            url = new QiNiuUtil().upload(fileInputStream, Constant.IMAGE);
        } else if (fileType.equals(FileTypeEnum.FILE)) {
            url = new QiNiuUtil().upload(fileInputStream, Constant.FILE);
        }
        FileVO fileVO = new FileVO();
        fileVO.setDownloadUrl(url);
        return fileVO;
    }

    public BaseVO uploadBlog(
            MultipartFile file,
            String fileType,
            WebsiteBlogList blog,
            String token
    ) throws Exception {
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


    /**
     * 上传素材图片
     * @param file 文件
     * @param token TOKEN
     * @param title 标题
     * @return 文件对象
     */
    public BaseVO uploadMeme(
            MultipartFile file,
            String token,
            String title
    ) throws Exception {
        // 判断是否是管理员
        if (!tokenRedisHandler.isAdmin(token)) {
            throw new BadRequestDataException("只有管理员才能上传素材图片");
        }

        // 一般性文件鉴权
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BadRequestDataException("传入的文件名不能为空");
        }
        if (!this.validateFileName(fileName)) {
            throw new BadRequestDataException("文件名应仅包含汉字、字母、数字、下划线和点号");
        }
        FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
        String url = new QiNiuUtil().upload(fileInputStream, Constant.MEME);
        FileVO fileVO = new FileVO();
        fileVO.setDownloadUrl(url);

        // 生成meme对象写入数据库
        UUID uuid = UUID.randomUUID();
        MemeInfo meme = new MemeInfo();
        meme.setId(uuid.toString());
        meme.setImgUrl(fileVO.getDownloadUrl());
        meme.setTitle(title);
        meme.setLikeNum(0);
        memeInfoDao.insert(meme);

        return fileVO;
    }
}
