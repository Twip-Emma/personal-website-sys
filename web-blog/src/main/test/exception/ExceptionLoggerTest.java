package exception;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import top.twip.api.exception.BadRequestDataException;
import top.twip.blog.WebBlogApplication;

@SpringBootTest(classes = WebBlogApplication.class)
public class ExceptionLoggerTest {

    private final Logger logger = LoggerFactory.getLogger(ExceptionLoggerTest.class);

    @Test
    void exceptionLoggerTest_shouldReturnFail() throws Exception {
        throw new BadRequestDataException("这是报错");
    }
}
