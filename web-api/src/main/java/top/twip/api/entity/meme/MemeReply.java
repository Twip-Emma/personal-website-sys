package top.twip.api.entity.meme;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import top.twip.api.entity.user.WebsiteUserInfo;

@Data
@TableName("t_meme_reply")
public class MemeReply {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;
    private String memeId;
    private String userId;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private Long ctime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;
    @TableField(exist = false)
    private WebsiteUserInfo user;
}
