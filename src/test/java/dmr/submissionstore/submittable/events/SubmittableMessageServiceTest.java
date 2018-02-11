package dmr.submissionstore.submittable.events;

import dmr.submissionstore.common.Event;
import dmr.submissionstore.submittable.Submittable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
public class SubmittableMessageServiceTest {


    SubmittableMessageService submittableMessageService;

    @MockBean
    RabbitMessagingTemplate rabbitMessagingTemplate;
    final String exchangeName = "testexchange1";


    @Before
    public void buildUp() {
        submittableMessageService = new SubmittableMessageService(rabbitMessagingTemplate, exchangeName);
    }


    @Test
    public void test() {
        Submittable submittable = Submittable.builder()
                .document("{}")
                .documentType("testType")
                .build();

        submittableMessageService.notifyCrudEvent(submittable, Event.created);

        String expectedRoutingKey = "subs.submittable.testType.created";

        verify(rabbitMessagingTemplate).convertAndSend(exchangeName, expectedRoutingKey, submittable);
    }
}
