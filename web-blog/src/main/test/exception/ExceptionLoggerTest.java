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
            throw new BadRequestDataException("-测444改441277773123213码-报错");
        } catch (BadRequestDataException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    // 测试新增代码31231233333
}
