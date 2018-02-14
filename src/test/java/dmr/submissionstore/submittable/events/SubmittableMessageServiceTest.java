package dmr.submissionstore.submittable.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dmr.submissionstore.JsonHelper;
import dmr.submissionstore.common.Event;
import dmr.submissionstore.submittable.Submittable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
public class SubmittableMessageServiceTest {


    SubmittableMessageService submittableMessageService;

    @MockBean
    RabbitMessagingTemplate rabbitMessagingTemplate;


    @Before
    public void buildUp() {
        submittableMessageService = new SubmittableMessageService(rabbitMessagingTemplate);
    }


    @Test
    public void test() {
        Submittable submittable = new Submittable();
        submittable.setDocument(JsonHelper.stringToJsonNode("{}"));
        submittable.setDocumentType("testType");

        submittableMessageService.notifyCrudEvent(submittable, Event.created);

        String expectedRoutingKey = "subs.submittable.testType.created";

        verify(rabbitMessagingTemplate).convertAndSend("usi-2", expectedRoutingKey, submittable);
    }


}
