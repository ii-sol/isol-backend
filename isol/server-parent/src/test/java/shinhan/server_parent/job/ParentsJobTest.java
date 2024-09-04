package shinhan.server_parent.job;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ParentsJobTest {

    @Test
    public void testExecute() throws JobExecutionException {
        ParentsJob job = new ParentsJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

        assertDoesNotThrow(() -> job.execute(context));
        // 추가적인 검증을 수행할 수 있습니다.
    }
}
