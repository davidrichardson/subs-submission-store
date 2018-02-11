package dmr.submissionstore.submittable.events;

import dmr.submissionstore.common.Event;
import dmr.submissionstore.submittable.Ref;
import dmr.submissionstore.submittable.Submittable;
import dmr.submissionstore.submittable.UploadedFileRef;
import dmr.submissionstore.submittable.extractors.FileRefExtractor;
import dmr.submissionstore.submittable.extractors.RefExtractor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmittableEventHandler.class)
public class SubmittableEventHandlerTest {

    @Autowired
    SubmittableEventHandler submittableEventHandler;

    @MockBean
    RefExtractor refExtractor;

    @MockBean
    FileRefExtractor fileRefExtractor;

    @MockBean
    SubmittableMessageService submittableMessageService;

    Submittable testInput;

    @Before
    public void buildUp(){
        this.testInput = Submittable.builder()
                .document("{}")
                .documentType("exampleType")
                .uniqueName("name1")
                .build();

    }


    @Test
    public void testBeforeCreateCallsRefExtractors(){
        Collection<Ref> refs = Arrays.asList(
                Ref.builder().accession("blah").build()
        );

        Collection<UploadedFileRef> files = Arrays.asList(
                UploadedFileRef.builder().uploadedFile("mmmm.txt").build()
        );

        given(this.refExtractor.extractRefs("{}")).willReturn(refs);
        given(this.fileRefExtractor.extractFileRefs("{}")).willReturn(files);


        submittableEventHandler.handleBeforeCreate(testInput);

        assertThat(testInput.getRefs(),equalTo(refs));
        assertThat(testInput.getUploadedFileRefs(),equalTo(files));

    }

    @Test
    public void testBeforeSaveCallsRefExtractors(){
        Collection<Ref> refs = Arrays.asList(
                Ref.builder().accession("blah").build()
        );

        Collection<UploadedFileRef> files = Arrays.asList(
                UploadedFileRef.builder().uploadedFile("mmmm.txt").build()
        );

        given(this.refExtractor.extractRefs("{}")).willReturn(refs);
        given(this.fileRefExtractor.extractFileRefs("{}")).willReturn(files);


        submittableEventHandler.handleBeforeSave(testInput);

        assertThat(testInput.getRefs(),equalTo(refs));
        assertThat(testInput.getUploadedFileRefs(),equalTo(files));

    }

    @Test
    public void testAfterCreateCallsMessageService(){

        submittableEventHandler.handleAfterCreate(testInput);

        verify(submittableMessageService).notifyCrudEvent(testInput, Event.created);
    }

    @Test
    public void testAfterUpdateCallsMessageService(){

        submittableEventHandler.handleAfterUpdate(testInput);

        verify(submittableMessageService).notifyCrudEvent(testInput, Event.updated);
    }

    @Test
    public void testAfterDeleteCallsMessageService(){

        submittableEventHandler.handleAfterDelete(testInput);

        verify(submittableMessageService).notifyCrudEvent(testInput, Event.deleted);
    }

}
