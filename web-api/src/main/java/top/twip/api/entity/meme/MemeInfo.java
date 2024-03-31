package top.twip.api.entity.meme;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("t_meme_info")
public class MemeInfo {
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    private String imgUrl;
    private Integer likeNum;
    private String title;
    @TableField(fill = FieldFill.INSERT)
    private Long ctime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;
}
