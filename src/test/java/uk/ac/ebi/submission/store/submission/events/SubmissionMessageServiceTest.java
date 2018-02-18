package uk.ac.ebi.submission.store.submission.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.common.Exchange;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittable.Submittable;
import uk.ac.ebi.submission.store.submittable.events.SubmittableMessageService;

import static org.mockito.BDDMockito.verify;

@RunWith(SpringRunner.class)
public class SubmissionMessageServiceTest {


    SubmissionMessageService submissionMessageService;

    @MockBean
    RabbitMessagingTemplate rabbitMessagingTemplate;


    @Before
    public void buildUp() {
        submissionMessageService = new SubmissionMessageService(rabbitMessagingTemplate);
    }


    @Test
    public void test() {
        Submission submission= new Submission();
        submission.setUiData(JsonHelper.stringToJsonNode("{}"));
        submission.setTitle("foo");

        submissionMessageService.notifyCrudEvent(submission, CrudEvent.created);

        String expectedRoutingKey = "subs.submission.created";

        verify(rabbitMessagingTemplate).convertAndSend(Exchange.EXCHANGE_NAME, expectedRoutingKey, submission);
    }

}
