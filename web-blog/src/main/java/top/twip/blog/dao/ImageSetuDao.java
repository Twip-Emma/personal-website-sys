package top.twip.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.twip.api.entity.image.ImageSetu;

import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-06 13:55
 */

/**
 * 涩图查询
 */
public interface ImageSetuDao extends BaseMapper<ImageSetu> {

    // 在指定仓库查询
    @Select("select * from image_s_#{databaseNum}_500 limit #{index},1;")
    List<ImageSetu> userGetOneSetuByIndex(@Param("databaseNum")Integer databaseNum,
                                          @Param("index")Integer index);


    // 在指定仓库查询
    @Select("select * from image_x_#{databaseNum}_500 limit #{index},1;")
    List<ImageSetu> userGetOneSetuByIndexX(@Param("databaseNum")Integer databaseNum,
                                          @Param("index")Integer index);


    // 查询指定仓库的记录数量（s仓库）
    @Select("select count(*) from image_s_#{databaseNum}_#{databaseTotal}")
    Integer getCountByDatabaseName(@Param("databaseNum")Integer databaseNum,
                                   @Param("databaseTotal")Integer databaseTotal);


    // 查询指定仓库的记录数量（x仓库）
    @Select("select count(*) from image_x_#{databaseNum}_#{databaseTotal}")
    Integer getCountByDatabaseNameX(@Param("databaseNum")Integer databaseNum,
                                   @Param("databaseTotal")Integer databaseTotal);
}
