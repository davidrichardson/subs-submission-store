package uk.ac.ebi.submission.store.document;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;


@Component
@RequiredArgsConstructor
public class DocumentOperationControl {

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    //TODO status rules should be broken out into a separate resource

    public boolean isChangeable(Document document) {
        boolean isSubmittableChangeable = (document.getStatus() != null && document.getStatus().equals("Draft"));
        boolean isSubmissionChangeable = submissionOperationControl.isChangeable(document.getId());

        return (isSubmissionChangeable && isSubmittableChangeable);
    }

}
