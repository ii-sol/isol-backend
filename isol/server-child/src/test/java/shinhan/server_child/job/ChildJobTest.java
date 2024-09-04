package shinhan.server_child.job;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ChildJobTest {

    @Test
    public void testExecute() throws JobExecutionException {
        ChildJob job = new ChildJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

        assertDoesNotThrow(() -> job.execute(context));
        // 추가적인 검증을 수행할 수 있습니다.
    }
}
