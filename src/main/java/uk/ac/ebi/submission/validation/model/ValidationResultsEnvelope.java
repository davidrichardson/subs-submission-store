package uk.ac.ebi.submission.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResultsEnvelope {

    private final Collection<SingleValidationResult> singleValidationResults = new ArrayList<>();
    private int validationResultVersion;
    private String validationResultId;
    private String validationAuthor;

    public void addValidationResult(SingleValidationResult singleValidationResult){
        singleValidationResults.add(singleValidationResult);
    }
}
