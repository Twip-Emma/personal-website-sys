package feign;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import top.twip.api.feign.TestFeign;
import top.twip.blog.WebBlogApplication;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebBlogApplication.class)
class TestFeignTest {

    private final Logger logger = LoggerFactory.getLogger(TestFeignTest.class);
    @Resource
    private TestFeign testFeign;

    @Test
    void testHelloTest_shouldReturnSuccess() {
        String hello = testFeign.hello();
        logger.info(hello);
        assertEquals("hello market", hello);
    }
}
