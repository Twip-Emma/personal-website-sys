package top.twip.common.entity.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 11:05
 */
@Data
@TableName("website_blog_list")
public class WebsiteBlogList {
    @TableId(value = "user_id",type = IdType.ASSIGN_ID)
    private String id;

    private String title;
    private String content;
    private String firstPicture;
    private String flag;

    private Integer views;
    private Integer appreciation;

    private Integer ctime;

    private Integer mtime;

    private String description;
    private Integer typeId;
    private String typeName;
    private String typePicUrl;
    private String typeColor;
    private String tags;

    private String userId;
    private String comments;
    private String tagIds;
}
