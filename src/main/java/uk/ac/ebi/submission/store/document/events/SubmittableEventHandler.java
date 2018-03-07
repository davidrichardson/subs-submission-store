package uk.ac.ebi.submission.store.document.events;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.document.extractors.FileRefExtractor;
import uk.ac.ebi.submission.store.document.extractors.RefExtractor;

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
    public void handleBeforeCreate(Document document){
        document.setId(UUID.randomUUID().toString());

        log.debug("handle before create {}", document);
        log.info("handle before create document {}", document.getId());

        handleBeforeCreateOrSave(document);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Document document){
        handleBeforeCreateOrSave(document);
    }

    @HandleAfterCreate
    public void handleAfterCreate(Document document){
        log.debug("handle after create {}", document);
        log.info("handle after create document {}", document.getId());

        submittableMessageService.notifyCrudEvent(document, CrudEvent.created);
    }

    @HandleAfterSave
    public void handleAfterUpdate(Document document){
        log.debug("handle after save {}", document);
        log.info("handle after save document {}", document.getId());

        submittableMessageService.notifyCrudEvent(document, CrudEvent.updated);
    }

    @HandleAfterDelete
    public void handleAfterDelete(Document document){
        log.debug("handle after save {}", document);
        log.info("handle after delete document {}", document.getId());

        submittableMessageService.notifyCrudEvent(document, CrudEvent.deleted);
    }

    public void handleBeforeCreateOrSave(Document document){

        document.setRefs(
                refExtractor.extractRefs(document.getContent())
        );
        document.setUploadedFileRefs(
                fileRefExtractor.extractFileRefs(document.getContent())
        );


    }

}
