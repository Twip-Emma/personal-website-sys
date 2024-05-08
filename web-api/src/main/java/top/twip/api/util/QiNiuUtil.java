package top.twip.api.util;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import top.twip.api.entity.file.Constant;
import top.twip.api.exception.BadRequestDataException;

import java.io.FileInputStream;
import java.util.UUID;

/**
 * Description:
 *
 * @author bright
 * @date in 2020/12/3 17:39
 */
public class QiNiuUtil {

    /**
     * 将图片上传到七牛云
     */
    public String upload(FileInputStream file1, String fileType) throws Exception {
        // 压缩图片
        FileInputStream file = ImageUtil.compressImage(file1);

        // zone0华东区域,zone1是华北区域,zone2是华南区域
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        Auth auth = Auth.create(Constant.accessKey, Constant.secretKey);
        String upToken = null;
        String path = null;
        switch (fileType) {
            case Constant.IMAGE -> {
                upToken = auth.uploadToken(Constant.bucketPictureName);
                path = Constant.domainPicture;
            }
            case Constant.FILE, Constant.MEME -> {
                upToken = auth.uploadToken(Constant.bucketFileName);
                path = Constant.domainFile;
            }
            default -> throw new BadRequestDataException("意外的fileType:" + fileType);
        }
        UUID uuid = UUID.randomUUID();
        Response response = uploadManager.put(
                file,
                "user-file/" + uuid + ".png",
                upToken,
                null,
                null
        );
        // 解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return path + putRet.key;
    }
}

