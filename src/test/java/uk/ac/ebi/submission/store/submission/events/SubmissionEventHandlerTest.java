package uk.ac.ebi.submission.store.submission.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.verify;
import static org.hamcrest.core.IsNull.notNullValue;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmissionEventHandler.class)
public class SubmissionEventHandlerTest {

    @Autowired
    SubmissionEventHandler submissionEventHandler;


    @MockBean
    SubmissionMessageService submissionMessageService;

    @MockBean
    SubmissionMongoRepository submissionMongoRepository;

    Submission testInput;

    @Before
    public void buildUp() {
        this.testInput = new Submission();
        this.testInput.setUiData(JsonHelper.stringToJsonNode("{}"));
        this.testInput.setTitle("A title");
    }


    @Test
    public void testBeforeCreateSetsId() {

        submissionEventHandler.handleBeforeCreate(testInput);

        assertThat(testInput.getId(), notNullValue());
    }


    @Test
    public void testAfterCreateCallsMessageService() {

        submissionEventHandler.handleAfterCreate(testInput);

        verify(submissionMessageService).notifyCrudEvent(testInput, CrudEvent.created);
    }

    @Test
    public void testAfterUpdateCallsMessageService() {

        submissionEventHandler.handleAfterUpdate(testInput);

        verify(submissionMessageService).notifyCrudEvent(testInput, CrudEvent.updated);
    }

    @Test
    public void testAfterDeleteCallsMessageService() {

        submissionEventHandler.handleAfterDelete(testInput);

        verify(submissionMessageService).notifyCrudEvent(testInput, CrudEvent.deleted);
    }

}
