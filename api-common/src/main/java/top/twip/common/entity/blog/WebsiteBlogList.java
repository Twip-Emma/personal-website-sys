package top.twip.common.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.type.BlobTypeHandler;
import top.twip.common.entity.user.WebsiteUserInfo;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:05
 */
@Data
@TableName("website_blog_list")
public class WebsiteBlogList {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String title;
    @TableField(exist = false)
    private String content;
    private byte[] contentBytes;
    private String firstPicture;
    private String flag;

    private Integer views;
    private Integer appreciation;

    @TableField(fill = FieldFill.INSERT)
    private Long ctime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;

    private String description;
    private Integer typeId;
    private String typeName;
    private String typePicUrl;
    private String typeColor;
    private String tags;

    private String userId;
    private String comments;
    private String tagIds;

    @TableField(exist = false)
    private WebsiteUserInfo user;
}
