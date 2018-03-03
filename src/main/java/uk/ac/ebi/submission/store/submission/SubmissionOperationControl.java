package uk.ac.ebi.submission.store.submission;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubmissionOperationControl {

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    public boolean isChangeable(Submission submission) {
        return (submission.getStatus() != null && submission.getStatus().equals("Draft"));
    }

    public boolean isChangeable(String id) {
        Optional<Submission> submission = submissionMongoRepository.findById(id);

        if (submission.isPresent()){
            return this.isChangeable(submission.get());
        }
        else {
            throw new IllegalStateException("No submission found for id "+id+", cannot report if it is changeable");
        }

    }

    //TODO status rules should be broken out into a separate resource
    public Collection<String> availableStatuses(Submission submission) {
        if (submission.getStatus() != null && submission.getStatus().equals("Draft")) {
            return Arrays.asList("Submitted");
        }
        else {
            return Collections.emptyList();
        }
    }
}
