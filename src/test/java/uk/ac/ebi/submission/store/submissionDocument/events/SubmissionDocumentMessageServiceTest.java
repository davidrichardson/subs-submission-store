package uk.ac.ebi.submission.store.submissionDocument.events;

import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.messaging.Exchange;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.verify;

@RunWith(SpringRunner.class)
public class SubmissionDocumentMessageServiceTest {


    SubmissionDocumentMessageService submissionDocumentMessageService;

    @MockBean
    RabbitMessagingTemplate rabbitMessagingTemplate;


    @Before
    public void buildUp() {
        submissionDocumentMessageService = new SubmissionDocumentMessageService(rabbitMessagingTemplate);
    }


    @Test
    public void test() {
        SubmissionDocument submissionDocument = new SubmissionDocument();
        submissionDocument.setContent(JsonHelper.stringToJsonNode("{}"));
        submissionDocument.setDocumentType("testType");

        submissionDocumentMessageService.notifyCrudEvent(submissionDocument, CrudEvent.created);

        String expectedRoutingKey = "subs.submissionDocument.testType.created";

        verify(rabbitMessagingTemplate).convertAndSend(Exchange.EXCHANGE_NAME, expectedRoutingKey, submissionDocument);
    }

}
