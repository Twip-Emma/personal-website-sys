package top.twip.higanbana.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.twip.common.entity.blog.WebsiteBlogList;

import java.util.List;
import java.util.Map;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:22
 */
public interface WebsiteBlogListDao extends BaseMapper<WebsiteBlogList> {
    // 分页查询 + 模糊查询
    @Select("select * from website_blog_list where title like #{name} ORDER BY ctime desc limit #{s},#{l};")
    List<WebsiteBlogList> getBlogByName(String name, Integer s, Integer l);

    // 分页查询 + 模糊查询(查询个数)
    @Select("select count(*) from website_blog_list where title like #{name}")
    Integer getBlogCountByName(String name);

    // 查询博客总数和分类数量（饼图用）
    @Select(" SELECT" +
            "    COUNT(*) AS total_blogs," +
            "    (SELECT SUM(views) FROM website_blog_list WHERE user_id = #{userId}) AS total_views,"+
            "    SUM(CASE WHEN type_name = '项目开发' THEN 1 ELSE 0 END) AS project_count, "+
            "    SUM(CASE WHEN type_name = '知识发现' THEN 1 ELSE 0 END) AS knowledge_count," +
            "    SUM(CASE WHEN type_name = '日常生活' THEN 1 ELSE 0 END) AS daily_count," +
            "    SUM(CASE WHEN type_name = '学习笔记' THEN 1 ELSE 0 END) AS study_count" +
            " FROM" +
            "    website_blog_list" +
            " WHERE" +
            "    user_id = #{userId}")
    List<Map<String, Object>> getBlogCountsByUserId(@Param("userId") String userId);
}
