package uk.ac.ebi.submission.dispatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittable.Submittable;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionEnvelope {

    private Submission submission;

    private final Map<String,Collection<Submittable>> submittedItemsByDocumentType = new HashMap<>();
    private final Map<String,Collection<Submittable>> referencedItemsByDocumentType = new HashMap<>();

    public void addSubmittedItem(Submittable submittable){
        addSubmittableToMap(submittable,submittedItemsByDocumentType);
    }

    public void addReferencedItem(Submittable submittable){
        addSubmittableToMap(submittable,referencedItemsByDocumentType);
    }

    private void addSubmittableToMap(Submittable submittable, Map<String,Collection<Submittable>> map){
        String documentType = submittable.getDocumentType();

        if (!map.containsKey(documentType)){
            map.put(documentType,new ArrayList<>());
        }

        map.get(documentType).add(submittable);
    }


}
