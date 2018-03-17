package uk.ac.ebi.submission.store.submissionDocument.events;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.rest.SubmissionDocumentMongoRepository;
import uk.ac.ebi.submission.store.submissionDocument.ProcessingStatus;
import uk.ac.ebi.submission.store.submissionDocument.extractors.FileRefExtractor;
import uk.ac.ebi.submission.store.submissionDocument.extractors.RefExtractor;

import java.util.Optional;
import java.util.UUID;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class SubmissionDocumentEventHandler {

    @NonNull private RefExtractor refExtractor;
    @NonNull private FileRefExtractor fileRefExtractor;
    @NonNull private SubmissionDocumentMessageService submissionDocumentMessageService;
    @NonNull private SubmissionMongoRepository submissionMongoRepository;
    @NonNull private SubmissionDocumentMongoRepository submissionDocumentMongoRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(SubmissionDocument submissionDocument){
        submissionDocument.setId(UUID.randomUUID().toString());
        submissionDocument.setStatus(ProcessingStatus.Draft);

        if (submissionDocument.getSubmissionId() != null){
            Optional<Submission> submission = submissionMongoRepository.findById(submissionDocument.getSubmissionId());
            if (submission.isPresent()){
                submissionDocument.setTeam(submission.get().getTeam());
            }
        }

        log.debug("handle before create {}", submissionDocument);


        handleBeforeCreateOrSave(submissionDocument);
    }

    @HandleBeforeSave
    public void handleBeforeSave(SubmissionDocument submissionDocument){
        if (submissionDocument.getId() != null){
            Optional<SubmissionDocument> optionalStoredVersion = submissionDocumentMongoRepository.findById(submissionDocument.getId());

            if (optionalStoredVersion.isPresent()){
                SubmissionDocument storedVersion = optionalStoredVersion.get();

                submissionDocument.setSubmissionId( storedVersion.getSubmissionId() );
                submissionDocument.setTeam( storedVersion.getTeam() );

                if (submissionDocument.getStatus() == null){
                    submissionDocument.setStatus( storedVersion.getStatus() );
                }
            }
        }


        handleBeforeCreateOrSave(submissionDocument);
    }

    @HandleAfterCreate
    public void handleAfterCreate(SubmissionDocument submissionDocument){
        log.debug("handle after create {}", submissionDocument);


        submissionDocumentMessageService.notifyCrudEvent(submissionDocument, CrudEvent.created);
    }

    @HandleAfterSave
    public void handleAfterUpdate(SubmissionDocument submissionDocument){
        log.debug("handle after save {}", submissionDocument);


        submissionDocumentMessageService.notifyCrudEvent(submissionDocument, CrudEvent.updated);
    }

    @HandleAfterDelete
    public void handleAfterDelete(SubmissionDocument submissionDocument){
        log.debug("handle after save {}", submissionDocument);


        submissionDocumentMessageService.notifyCrudEvent(submissionDocument, CrudEvent.deleted);
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
