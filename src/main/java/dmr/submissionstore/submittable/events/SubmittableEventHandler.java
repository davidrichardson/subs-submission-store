package dmr.submissionstore.submittable.events;

import dmr.submissionstore.submittable.Submittable;
import dmr.submissionstore.submittable.services.FileRefExtractor;
import dmr.submissionstore.submittable.services.RefExtractor;
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

        //TODO rabbit event
    }

    @HandleAfterSave
    public void handleAfterUpdate(Submittable submittable){
        log.debug("handle after save {}",submittable);
        log.info("handle after save submittable {}",submittable.getId());

        //TODO rabbit event
    }

    @HandleAfterDelete
    public void handleAfterDelete(Submittable submittable){
        log.debug("handle after save {}",submittable);
        log.info("handle after delete submittable {}",submittable.getId());

        //TODO rabbit event
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
