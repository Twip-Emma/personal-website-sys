package top.twip.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.twip.api.entity.blog.WebImageEntity;
import top.twip.api.entity.blog.WebsiteBlogList;

import java.util.List;

public interface WebImageDao extends BaseMapper<WebImageEntity> {

    /**
     * 根据类型获取随机一张图片
     */
    @Select("SELECT * FROM t_web_image WHERE type=#{type} ORDER BY RAND() LIMIT 1;")
    List<WebImageEntity> getRandomImageByType(String type);

}
