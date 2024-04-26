package top.twip.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.twip.api.entity.meme.MemeInfo;

public interface MemeInfoDao extends BaseMapper<MemeInfo> {

    @Update("update t_meme_info set like_num = like_num + 1 where id = #{id, jdbcType=VARCHAR}")
    void incLike(@Param("id")String memeId);

    @Update("update t_meme_info set like_num = like_num - 1 where id = #{id, jdbcType=VARCHAR}")
    void desLike(@Param("id")String memeId);
}
