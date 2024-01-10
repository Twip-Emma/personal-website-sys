package top.twip.api.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "website_user_avatar")
public class WebsiteAvatarEntity {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String url;
    private String name;
}
