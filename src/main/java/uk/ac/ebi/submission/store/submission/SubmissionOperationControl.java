package uk.ac.ebi.submission.store.submission;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SubmissionOperationControl {

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @NonNull
    private Map<String, StatusDescription> submissionStatusDescriptionMap;

    public boolean isChangeable(Submission submission) {
        Assert.notNull(submission);
        Assert.notNull(submission.getStatus());

        StatusDescription statusDescription = statusDescriptionForSubmission(submission);

        return statusDescription.isAcceptingUpdates();
    }

    private StatusDescription statusDescriptionForSubmission(Submission submission) {
        String statusName = submission.getStatus().name();

        return submissionStatusDescriptionMap.get(statusName);
    }

    public boolean isChangeable(String id) {
        Optional<Submission> submission = submissionMongoRepository.findById(id);

        if (submission.isPresent()){
            return this.isChangeable(submission.get());
        }
        else {
            throw new IllegalArgumentException("No submission found for id "+id+", cannot report if it is changeable");
        }

    }

    public Collection<String> availableStatuses(Submission submission) {
        StatusDescription statusDescription = statusDescriptionForSubmission(submission);

        return statusDescription.getUserTransitions();
    }
}
