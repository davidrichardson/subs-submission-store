package dmr.submissionstore.submission;

import org.springframework.stereotype.Component;

@Component
public class SubmissionOperationControl {

    public boolean isChangeable(Submission submission){
        return (submission.getStatus() != null && submission.getStatus().equals("Draft"));
    }
}
