package top.twip.blog.service;

import org.springframework.stereotype.Service;
import top.twip.api.entity.blog.WebImageEntity;
import top.twip.blog.dao.WebImageDao;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WebImageService {

    @Resource
    private WebImageDao webImageDao;

    /**
     * 获取登录界面的随机图片
     * @return String 图片URL
     */
    public String getRandomImageByType(String type) {
        List<WebImageEntity> images = webImageDao.getRandomImageByType(type);
        if (images.size() == 0){
            return null;
        } else {
            return images.get(0).getUrl();
        }
    }
}
