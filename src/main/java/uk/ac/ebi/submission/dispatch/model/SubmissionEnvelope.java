package uk.ac.ebi.submission.dispatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submission.Submission;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionEnvelope {

    private Submission submission;

    private final Map<String,Collection<SubmissionDocument>> submittedItemsByDocumentType = new HashMap<>();
    private final Map<String,Collection<SubmissionDocument>> referencedItemsByDocumentType = new HashMap<>();

    public void addSubmittedItem(SubmissionDocument submissionDocument){
        addDocumentToMap(submissionDocument,submittedItemsByDocumentType);
    }

    public void addReferencedItem(SubmissionDocument submissionDocument){
        addDocumentToMap(submissionDocument,referencedItemsByDocumentType);
    }

    private void addDocumentToMap(SubmissionDocument submissionDocument, Map<String,Collection<SubmissionDocument>> map){
        String documentType = submissionDocument.getDocumentType();

        if (!map.containsKey(documentType)){
            map.put(documentType,new ArrayList<>());
        }

        map.get(documentType).add(submissionDocument);
    }


}
