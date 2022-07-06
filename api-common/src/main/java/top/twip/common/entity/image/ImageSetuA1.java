package top.twip.common.entity.image;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-05 22:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("image_a_1_500")
public class ImageSetuA1 extends ImageSetu{
    private String imageId;
    private byte[] content;
    private String realName;
}
