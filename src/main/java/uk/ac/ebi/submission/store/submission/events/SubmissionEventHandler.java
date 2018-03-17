package uk.ac.ebi.submission.store.submission.events;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.submission.SubmissionStatus;

import java.util.Optional;
import java.util.UUID;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class SubmissionEventHandler {

    @NonNull
    private SubmissionMessageService submissionMessageService;

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(Submission submission){
        submission.setId(UUID.randomUUID().toString());
        submission.setStatus(SubmissionStatus.Draft);
        log.debug("handle before create {}",submission);

    }

    @HandleAfterCreate
    public void handleAfterCreate(Submission submission) {
        log.debug("handle after create {}", submission);


        submissionMessageService.notifyCrudEvent(submission, CrudEvent.created);
    }

    @HandleAfterDelete
    public void handleAfterDelete(Submission submission) {
        log.debug("handle after create {}", submission);


        submissionMessageService.notifyCrudEvent(submission, CrudEvent.deleted);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Submission submission){
        Optional<Submission> storedVersion = submissionMongoRepository.findById(submission.getId());
        if (storedVersion.isPresent()){
            submission.setTeam(submission.getTeam());
        }
    }

    @HandleAfterSave
    public void handleAfterUpdate(Submission submission) {
        log.debug("handle after create {}", submission);


        submissionMessageService.notifyCrudEvent(submission, CrudEvent.updated);
    }
}
