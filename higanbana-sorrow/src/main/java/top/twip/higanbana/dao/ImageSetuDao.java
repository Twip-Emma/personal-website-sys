package top.twip.higanbana.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.twip.common.entity.image.ImageSetu;

import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-06 13:55
 */
public interface ImageSetuDao extends BaseMapper<ImageSetu> {

    // 在指定仓库查询
    @Select("select * from image_s_#{databaseNum}_500 limit #{index},1;")
    List<ImageSetu> userGetOneSetuByIndex(@Param("databaseNum")Integer databaseNum,
                                          @Param("index")Integer index);
}
