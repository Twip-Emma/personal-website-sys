package top.twip.common.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 10:56
 */
@Data
@TableName("website_user_info")
public class WebsiteUserInfo {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String nickname;
    private String card;
    private String pass;
    private String avatar;
    private String qqemail;
    private Integer isadmin;

    @TableField(fill = FieldFill.INSERT)
    private Long ctime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;

    @TableField(exist = false)
    private String token;
}
