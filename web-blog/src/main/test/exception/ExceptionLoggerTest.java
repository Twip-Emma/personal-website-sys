package exception;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.twip.api.exception.BadRequestDataException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionLoggerTest {

    private final Logger logger = LoggerFactory.getLogger(ExceptionLoggerTest.class);

    @Test
    void exceptionLoggerTest_shouldReturnFail() throws Exception {
        boolean thrown = false;
        try {
            throw new BadRequestDataException("这是报错");
        } catch (BadRequestDataException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
