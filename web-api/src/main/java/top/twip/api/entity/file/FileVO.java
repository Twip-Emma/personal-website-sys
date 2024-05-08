package top.twip.api.entity.file;


import lombok.Data;

import java.io.Serial;

/**
 * Description:
 *
 * @author bright
 * @date in 2020/11/16 13:57
 */
@Data
public class FileVO extends BaseVO {
    @Serial
    private static final long serialVersionUID = 1L;

    private String downloadUrl;
}

