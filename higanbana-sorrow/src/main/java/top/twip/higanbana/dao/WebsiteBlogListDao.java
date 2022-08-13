package top.twip.higanbana.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.twip.common.entity.blog.WebsiteBlogList;

import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:22
 */
public interface WebsiteBlogListDao extends BaseMapper<WebsiteBlogList> {
    // 分页查询 + 模糊查询
    @Select("select * from website_blog_list where title like #{name} limit #{s},#{l};")
    List<WebsiteBlogList> getBlogByName(String name, Integer s, Integer l);

    // 分页查询 + 模糊查询(查询个数)
    @Select("select count(*) from website_blog_list where title like #{name}")
    Integer getBlogCountByName(String name);
}
