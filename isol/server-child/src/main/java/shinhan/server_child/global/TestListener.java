package shinhan.server_child.global;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestListener {
    @RabbitListener(queues = "test.queue")
    public void reciveMessage(final Message message)
    {
        System.out.println(message.toString());
    }

}
