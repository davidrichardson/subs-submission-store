package uk.ac.ebi.submission.store.submissionDocument.events;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.extractors.FileRefExtractor;
import uk.ac.ebi.submission.store.submissionDocument.extractors.RefExtractor;

import java.util.UUID;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class SubmittableEventHandler {

    @NonNull private RefExtractor refExtractor;
    @NonNull private FileRefExtractor fileRefExtractor;
    @NonNull private SubmittableMessageService submittableMessageService;

    @HandleBeforeCreate
    public void handleBeforeCreate(SubmissionDocument submissionDocument){
        submissionDocument.setId(UUID.randomUUID().toString());

        log.debug("handle before create {}", submissionDocument);
        log.info("handle before create submissionDocument {}", submissionDocument.getId());

        handleBeforeCreateOrSave(submissionDocument);
    }

    @HandleBeforeSave
    public void handleBeforeSave(SubmissionDocument submissionDocument){
        handleBeforeCreateOrSave(submissionDocument);
    }

    @HandleAfterCreate
    public void handleAfterCreate(SubmissionDocument submissionDocument){
        log.debug("handle after create {}", submissionDocument);
        log.info("handle after create submissionDocument {}", submissionDocument.getId());

        submittableMessageService.notifyCrudEvent(submissionDocument, CrudEvent.created);
    }

    @HandleAfterSave
    public void handleAfterUpdate(SubmissionDocument submissionDocument){
        log.debug("handle after save {}", submissionDocument);
        log.info("handle after save submissionDocument {}", submissionDocument.getId());

        submittableMessageService.notifyCrudEvent(submissionDocument, CrudEvent.updated);
    }

    @HandleAfterDelete
    public void handleAfterDelete(SubmissionDocument submissionDocument){
        log.debug("handle after save {}", submissionDocument);
        log.info("handle after delete submissionDocument {}", submissionDocument.getId());

        submittableMessageService.notifyCrudEvent(submissionDocument, CrudEvent.deleted);
    }

    public void handleBeforeCreateOrSave(SubmissionDocument submissionDocument){

        submissionDocument.setRefs(
                refExtractor.extractRefs(submissionDocument.getContent())
        );
        submissionDocument.setUploadedFileRefs(
                fileRefExtractor.extractFileRefs(submissionDocument.getContent())
        );


    }

}
