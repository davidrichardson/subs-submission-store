package uk.ac.ebi.submission.dispatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.submission.Submission;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionEnvelope {

    private Submission submission;

    private final Map<String,Collection<Document>> submittedItemsByDocumentType = new HashMap<>();
    private final Map<String,Collection<Document>> referencedItemsByDocumentType = new HashMap<>();

    public void addSubmittedItem(Document document){
        addDocumentToMap(document,submittedItemsByDocumentType);
    }

    public void addReferencedItem(Document document){
        addDocumentToMap(document,referencedItemsByDocumentType);
    }

    private void addDocumentToMap(Document document, Map<String,Collection<Document>> map){
        String documentType = document.getDocumentType();

        if (!map.containsKey(documentType)){
            map.put(documentType,new ArrayList<>());
        }

        map.get(documentType).add(document);
    }


}
