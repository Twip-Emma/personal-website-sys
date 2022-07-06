package top.twip.common.entity.image;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-05 22:50
 */
@Data
@TableName("image_s_1_500")
public class ImageSetuS1{
    private String imageId;
    private byte[] content;
    private String realName;
}
