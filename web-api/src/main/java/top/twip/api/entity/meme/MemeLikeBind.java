package top.twip.api.entity.meme;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 中间表：绑定用户与meme，用于判断该用户对某个meme是否进行了点赞功能
 */
@Data
@TableName("t_meme_like")
public class MemeLikeBind {
    private String userId;
    private String memeId;
    @TableField(fill = FieldFill.INSERT)
    private Long ctime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long mtime;
}
