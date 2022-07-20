package top.twip.common.entity.blog;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import top.twip.common.entity.user.WebsiteUserInfo;

@Data
public class WebsiteBlogInfoEntity {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String title; // 标题
    private String content; // 正文
    private String firstPicture; // 镇楼图
    private String flag; // 文章标签，比如：原创、借鉴
    private Integer views; // 浏览量

    @TableField(fill = FieldFill.INSERT)
    private Long ctime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;

    private String description; // 文章简介

    // 标签复写到列表
    private Integer typeId;
    private String typeName;
    private String typePicUrl;
    private String typeColor;
    private String tags; // 一串字符串，以|分隔，交给前端解析

    // 所绑定的用户信息
    private String userId; // 用户id

    @TableField(exist = false)
    private WebsiteUserInfo user;
}
