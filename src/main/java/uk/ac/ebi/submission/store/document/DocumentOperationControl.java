package uk.ac.ebi.submission.store.document;


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
public class DocumentOperationControl {

    @NonNull
    private Map<String, StatusDescription> documentStatusDescriptionMap;;

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    public boolean isChangeable(Document document) {
        return (isDocumentChangeable(document) && isSubmissionChangeable(document));
    }

    private boolean isDocumentChangeable(Document document) {
        Assert.notNull(document.getStatus(),"Document must have a status: "+document);
        String statusName = document.getStatus().name();
        StatusDescription statusDescription = documentStatusDescriptionMap.get(statusName);

        Assert.notNull(statusDescription,"Document status must exist in config:  "+document);

        return statusDescription.isAcceptingUpdates();
    }

    private boolean isSubmissionChangeable(Document document) {
        return submissionOperationControl.isChangeable(document.getId());
    }

}
