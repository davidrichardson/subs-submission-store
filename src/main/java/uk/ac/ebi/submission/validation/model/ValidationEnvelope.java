package uk.ac.ebi.submission.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationEnvelope {


    private String validationResultId;
    private int validationResultVersion;

    private SubmissionDocument entityToValidate;
    private String submissionId;

    private final Map<String, Collection<SubmissionDocument>> referencedItemsByDocumentType = new HashMap<>();

    private void referencedItemsByDocumentType(SubmissionDocument submissionDocument) {
        String documentType = submissionDocument.getDocumentType();

        if (!referencedItemsByDocumentType.containsKey(documentType)) {
            referencedItemsByDocumentType.put(documentType, new ArrayList<>());
        }

        referencedItemsByDocumentType.get(documentType).add(submissionDocument);
    }
}
