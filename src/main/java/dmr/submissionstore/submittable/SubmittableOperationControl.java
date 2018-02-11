package dmr.submissionstore.submittable;


import dmr.submissionstore.submission.Submission;
import org.springframework.stereotype.Component;


@Component
public class SubmittableOperationControl {

    public boolean isChangeable(Submittable submittable) {
        return (submittable.getStatus() != null && submittable.getStatus().equals("Draft"));
    }
}
