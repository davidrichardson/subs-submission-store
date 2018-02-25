package uk.ac.ebi.submission.store.submittable.events;

import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.messaging.Exchange;
import uk.ac.ebi.submission.store.submittable.Submittable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.verify;

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

        submittableMessageService.notifyCrudEvent(submittable, CrudEvent.created);

        String expectedRoutingKey = "subs.submittable.testType.created";

        verify(rabbitMessagingTemplate).convertAndSend(Exchange.EXCHANGE_NAME, expectedRoutingKey, submittable);
    }

}
