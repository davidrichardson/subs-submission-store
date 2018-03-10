package uk.ac.ebi.submission.store.document.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.document.Ref;
import uk.ac.ebi.submission.store.document.UploadedFileRef;
import uk.ac.ebi.submission.store.document.extractors.FileRefExtractor;
import uk.ac.ebi.submission.store.document.extractors.RefExtractor;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmittableEventHandler.class)
public class DocumentEventHandlerTest {

    @Autowired
    SubmittableEventHandler submittableEventHandler;

    @MockBean
    RefExtractor refExtractor;

    @MockBean
    FileRefExtractor fileRefExtractor;

    @MockBean
    SubmittableMessageService submittableMessageService;

    Document testInput;

    @Before
    public void buildUp() {
        this.testInput = new Document();
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


        submittableEventHandler.handleBeforeCreate(testInput);

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


        submittableEventHandler.handleBeforeSave(testInput);

        assertThat(testInput.getRefs(), equalTo(refs));
        assertThat(testInput.getUploadedFileRefs(), equalTo(files));

    }

    @Test
    public void testAfterCreateCallsMessageService() {

        submittableEventHandler.handleAfterCreate(testInput);

        verify(submittableMessageService).notifyCrudEvent(testInput, CrudEvent.created);
    }

    @Test
    public void testAfterUpdateCallsMessageService() {

        submittableEventHandler.handleAfterUpdate(testInput);

        verify(submittableMessageService).notifyCrudEvent(testInput, CrudEvent.updated);
    }

    @Test
    public void testAfterDeleteCallsMessageService() {

        submittableEventHandler.handleAfterDelete(testInput);

        verify(submittableMessageService).notifyCrudEvent(testInput, CrudEvent.deleted);
    }

}