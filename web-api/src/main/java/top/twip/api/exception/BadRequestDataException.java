package top.twip.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 七画一只妖
 * @Date: 2022-06-22 9:07
 */
public class BadRequestDataException extends Exception {

    private final Logger logger = LoggerFactory.getLogger(BadRequestDataException.class);

    public BadRequestDataException(String message) {
        super(message);
        logger.error(message);
    }
}
