package uk.ac.ebi.submission.store.document.events;

import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.messaging.Exchange;
import uk.ac.ebi.submission.store.document.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.verify;

@RunWith(SpringRunner.class)
public class DocumentMessageServiceTest {


    SubmittableMessageService submittableMessageService;

    @MockBean
    RabbitMessagingTemplate rabbitMessagingTemplate;


    @Before
    public void buildUp() {
        submittableMessageService = new SubmittableMessageService(rabbitMessagingTemplate);
    }


    @Test
    public void test() {
        Document document = new Document();
        document.setContent(JsonHelper.stringToJsonNode("{}"));
        document.setDocumentType("testType");

        submittableMessageService.notifyCrudEvent(document, CrudEvent.created);

        String expectedRoutingKey = "subs.document.testType.created";

        verify(rabbitMessagingTemplate).convertAndSend(Exchange.EXCHANGE_NAME, expectedRoutingKey, document);
    }

}
