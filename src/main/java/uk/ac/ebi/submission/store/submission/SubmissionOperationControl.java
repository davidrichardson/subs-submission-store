package uk.ac.ebi.submission.store.submission;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class SubmissionOperationControl {

    //TODO status rules should be broken out into a separate resource

    public boolean isChangeable(Submission submission) {
        return (submission.getStatus() != null && submission.getStatus().equals("Draft"));
    }

    public Collection<String> availableStatuses(Submission submission) {
        if (submission.getStatus() != null && submission.getStatus().equals("Draft")) {
            return Arrays.asList("Submitted");
        }
        else {
            return Collections.emptyList();
        }
    }
}
