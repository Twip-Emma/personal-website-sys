package top.twip.common.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "website_message_reply")
public class WebsiteMessageEntity {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String userId;
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Long ctime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String nickname;
}
