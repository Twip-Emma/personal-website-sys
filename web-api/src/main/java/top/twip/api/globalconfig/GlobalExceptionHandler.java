package top.twip.api.globalconfig;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.twip.api.constant.FeignConstants;
import top.twip.api.enums.CodeEnum;
import top.twip.api.exception.BadRequestDataException;
import top.twip.api.exception.DatabaseDataNotFound;
import top.twip.api.exception.DatabaseHandlerException;
import top.twip.api.exception.OperationErrorException;
import top.twip.api.response.DataFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 七画一只妖
 * @Date: 2022-06-22 8:46
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    private Object handleException(HttpServletRequest request, Exception e) {
        logger.error(e.getMessage());

        // 当本次请求是feign调用，则不进入错误处理逻辑
        if (request.getHeader(FeignConstants.HEADER_NAME.getValue()) != null) {
            return null;
        }

        // 错误状态码默认500
        CodeEnum code = CodeEnum.INTERNAL_ERROR;
        if (e instanceof DatabaseDataNotFound){
            code = CodeEnum.NOT_FOUND;
        } else if(e instanceof DatabaseHandlerException){
            code = CodeEnum.BAD_REQUEST;
        } else if(e instanceof BadRequestDataException){
            code = CodeEnum.NOT_ALL_OK;
        } else if(e instanceof OperationErrorException){
            code = CodeEnum.USER_HANDLE_ERROR;
        }
        return DataFactory.fail(code,e.getMessage());
    }
}
