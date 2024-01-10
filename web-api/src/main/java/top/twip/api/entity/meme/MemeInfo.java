package top.twip.api.entity.meme;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_meme_info")
public class MemeInfo {
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    private String imgUrl;
    private Integer likeNum;
    private String title;
}
