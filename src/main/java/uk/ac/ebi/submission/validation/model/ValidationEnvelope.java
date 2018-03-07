package uk.ac.ebi.submission.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.submission.store.document.Document;

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

    private Document entityToValidate;
    private String submissionId;

    private final Map<String, Collection<Document>> referencedItemsByDocumentType = new HashMap<>();

    private void referencedItemsByDocumentType(Document document) {
        String documentType = document.getDocumentType();

        if (!referencedItemsByDocumentType.containsKey(documentType)) {
            referencedItemsByDocumentType.put(documentType, new ArrayList<>());
        }

        referencedItemsByDocumentType.get(documentType).add(document);
    }
}
