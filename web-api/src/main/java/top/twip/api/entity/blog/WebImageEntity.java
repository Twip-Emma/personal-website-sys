package top.twip.api.entity.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_web_image")
public class WebImageEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String type;

    private String url;
}
