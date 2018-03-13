package uk.ac.ebi.submission.store.submissionDocument;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class SubmissionDocumentOperationControl {

    @NonNull
    private Map<String, StatusDescription> documentStatusDescriptionMap;;

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    public boolean isChangeable(SubmissionDocument submissionDocument) {
        return (isDocumentChangeable(submissionDocument) && isSubmissionChangeable(submissionDocument));
    }

    private boolean isDocumentChangeable(SubmissionDocument submissionDocument) {
        Assert.notNull(submissionDocument.getStatus(),"SubmissionDocument must have a status: "+ submissionDocument);
        String statusName = submissionDocument.getStatus().name();
        StatusDescription statusDescription = documentStatusDescriptionMap.get(statusName);

        Assert.notNull(statusDescription,"SubmissionDocument status must exist in config:  "+ submissionDocument);

        return statusDescription.isAcceptingUpdates();
    }

    private boolean isSubmissionChangeable(SubmissionDocument submissionDocument) {
        return submissionDocument.getSubmissionId() != null && submissionOperationControl.isChangeable(submissionDocument.getSubmissionId());
    }

}
