package uk.ac.ebi.submission.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.submission.store.submittable.Submittable;

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

    private Submittable entityToValidate;
    private String submissionId;

    private final Map<String, Collection<Submittable>> referencedItemsByDocumentType = new HashMap<>();

    private void referencedItemsByDocumentType(Submittable submittable) {
        String documentType = submittable.getDocumentType();

        if (!referencedItemsByDocumentType.containsKey(documentType)) {
            referencedItemsByDocumentType.put(documentType, new ArrayList<>());
        }

        referencedItemsByDocumentType.get(documentType).add(submittable);
    }
}
