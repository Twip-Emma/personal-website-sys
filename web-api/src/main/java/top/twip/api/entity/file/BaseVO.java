package top.twip.api.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author bright
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean success = true;

    private String errorCode;

    private String errorMsg;
}


