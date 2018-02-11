package dmr.submissionstore.submittable.events;

import dmr.submissionstore.common.Event;
import dmr.submissionstore.submittable.Submittable;
import dmr.submissionstore.submittable.extractors.FileRefExtractor;
import dmr.submissionstore.submittable.extractors.RefExtractor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;

import java.util.UUID;

@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class SubmittableEventHandler {

    @NonNull private RefExtractor refExtractor;
    @NonNull private FileRefExtractor fileRefExtractor;
    @NonNull private SubmittableMessageService submittableMessageService;

    @HandleBeforeCreate
    public void handleBeforeCreate(Submittable submittable){
        submittable.setId(UUID.randomUUID().toString());

        log.debug("handle before create {}",submittable);
        log.info("handle before create submittable {}",submittable.getId());

        handleBeforeCreateOrSave(submittable);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Submittable submittable){
        handleBeforeCreateOrSave(submittable);
    }

    @HandleAfterCreate
    public void handleAfterCreate(Submittable submittable){
        log.debug("handle after create {}",submittable);
        log.info("handle after create submittable {}",submittable.getId());

        submittableMessageService.notifyCrudEvent(submittable, Event.created);
    }

    @HandleAfterSave
    public void handleAfterUpdate(Submittable submittable){
        log.debug("handle after save {}",submittable);
        log.info("handle after save submittable {}",submittable.getId());

        submittableMessageService.notifyCrudEvent(submittable, Event.updated);
    }

    @HandleAfterDelete
    public void handleAfterDelete(Submittable submittable){
        log.debug("handle after save {}",submittable);
        log.info("handle after delete submittable {}",submittable.getId());

        submittableMessageService.notifyCrudEvent(submittable, Event.deleted);
    }

    public void handleBeforeCreateOrSave(Submittable submittable){

        submittable.setRefs(
                refExtractor.extractRefs(submittable.getDocument())
        );
        submittable.setUploadedFileRefs(
                fileRefExtractor.extractFileRefs(submittable.getDocument())
        );


    }

}
