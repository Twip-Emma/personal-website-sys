package top.twip.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.twip.api.entity.file.Constant;

@Configuration
@ConfigurationProperties(prefix = "oss.qiniu")
@Data
@RefreshScope
public class OssInitConfig {
    /**
     * AccessKey
     */
    private String accessKey;
    /**
     * SecretKey
     */
    private String secretKey;
    /**
     * 图片存储空间名
     */
    private String bucketPictureName;
    /**
     * 图片外链
     */
    private String domainPicture;
    /**
     * 文件存储空间名
     */
    private String bucketFileName;
    /**
     * 文件外链
     */
    private String domainFile;

    @Bean
    public void init() {
        Constant.accessKey = this.accessKey;
        Constant.secretKey = this.secretKey;
        Constant.bucketPictureName = this.bucketPictureName;
        Constant.domainPicture = this.domainPicture;
        Constant.bucketFileName = this.bucketFileName;
        Constant.domainFile = this.domainFile;
    }
}
