package uk.ac.ebi.submission.store.submission;

import org.springframework.stereotype.Component;

@Component
public class SubmissionOperationControl {

    //TODO status rules should be broken out into a separate resource

    public boolean isChangeable(Submission submission){
        return (submission.getStatus() != null && submission.getStatus().equals("Draft"));
    }
}
