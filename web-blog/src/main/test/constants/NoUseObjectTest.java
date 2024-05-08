package constants;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.twip.api.constant.NoValueConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoUseObjectTest {
    private final Logger logger = LoggerFactory.getLogger(NoUseObjectTest.class);

    @Test
    void loggingNoUse_shouldReturnSuccess() {
        assertEquals("card", NoValueConstants.INDEX);
        assertEquals("token", NoValueConstants.TOKEN_HEADER);
    }
}
