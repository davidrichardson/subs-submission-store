package uk.ac.ebi.submission.store.submissionDocument.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.Ref;
import uk.ac.ebi.submission.store.submissionDocument.UploadedFileRef;
import uk.ac.ebi.submission.store.submissionDocument.extractors.FileRefExtractor;
import uk.ac.ebi.submission.store.submissionDocument.extractors.RefExtractor;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmissionDocumentEventHandler.class)
public class SubmissionDocumentEventHandlerTest {

    @Autowired
    SubmissionDocumentEventHandler submissionDocumentEventHandler;

    @MockBean
    RefExtractor refExtractor;

    @MockBean
    FileRefExtractor fileRefExtractor;

    @MockBean
    SubmissionDocumentMessageService submissionDocumentMessageService;

    SubmissionDocument testInput;

    @Before
    public void buildUp() {
        this.testInput = new SubmissionDocument();
        this.testInput.setContent(JsonHelper.stringToJsonNode("{}"));
        this.testInput.setDocumentType("exampleType");
        this.testInput.setUniqueName("name1");
    }

    private Ref ref() {
        Ref r = new Ref();
        r.setAccession("blah");
        return r;
    }


    @Test
    public void testBeforeCreateCallsRefExtractors() {

        Collection<Ref> refs = Arrays.asList(
                ref()
        );

        Collection<UploadedFileRef> files = Arrays.asList(
                uploadedFileRef()
        );

        given(this.refExtractor.extractRefs("{}")).willReturn(refs);
        given(this.fileRefExtractor.extractFileRefs("{}")).willReturn(files);


        submissionDocumentEventHandler.handleBeforeCreate(testInput);

        assertThat(testInput.getRefs(), equalTo(refs));
        assertThat(testInput.getUploadedFileRefs(), equalTo(files));

    }

    private UploadedFileRef uploadedFileRef() {
        UploadedFileRef u = new UploadedFileRef();
        u.setUploadedFile("mmmm.txt");
        return u;
    }

    @Test
    public void testBeforeSaveCallsRefExtractors() {
        Collection<Ref> refs = Arrays.asList(
                ref()
        );

        Collection<UploadedFileRef> files = Arrays.asList(
                uploadedFileRef()
        );

        given(this.refExtractor.extractRefs("{}")).willReturn(refs);
        given(this.fileRefExtractor.extractFileRefs("{}")).willReturn(files);


        submissionDocumentEventHandler.handleBeforeSave(testInput);

        assertThat(testInput.getRefs(), equalTo(refs));
        assertThat(testInput.getUploadedFileRefs(), equalTo(files));

    }

    @Test
    public void testAfterCreateCallsMessageService() {

        submissionDocumentEventHandler.handleAfterCreate(testInput);

        verify(submissionDocumentMessageService).notifyCrudEvent(testInput, CrudEvent.created);
    }

    @Test
    public void testAfterUpdateCallsMessageService() {

        submissionDocumentEventHandler.handleAfterUpdate(testInput);

        verify(submissionDocumentMessageService).notifyCrudEvent(testInput, CrudEvent.updated);
    }

    @Test
    public void testAfterDeleteCallsMessageService() {

        submissionDocumentEventHandler.handleAfterDelete(testInput);

        verify(submissionDocumentMessageService).notifyCrudEvent(testInput, CrudEvent.deleted);
    }

}
