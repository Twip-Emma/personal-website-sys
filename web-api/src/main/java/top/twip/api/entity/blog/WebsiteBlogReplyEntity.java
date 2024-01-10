package top.twip.api.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-11 11:09
 */
@Data
@TableName(value = "website_blog_reply")
public class WebsiteBlogReplyEntity {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String userId;
    private String articleId;
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
