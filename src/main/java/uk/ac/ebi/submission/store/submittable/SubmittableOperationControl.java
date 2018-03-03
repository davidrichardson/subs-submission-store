package uk.ac.ebi.submission.store.submittable;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;

import java.util.Collection;
import java.util.Collections;


@Component
@RequiredArgsConstructor
public class SubmittableOperationControl {

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    //TODO status rules should be broken out into a separate resource

    public boolean isChangeable(Submittable submittable) {
        boolean isSubmittableChangeable = (submittable.getStatus() != null && submittable.getStatus().equals("Draft"));
        boolean isSubmissionChangeable = submissionOperationControl.isChangeable(submittable.getId());

        return (isSubmissionChangeable && isSubmittableChangeable);
    }

}
