package uk.ac.ebi.submission.store.validationResult;

import lombok.Data;

@Data
public class SingleValidationResult {

    private String author;
    private String status;

    private String message;
    private String jsonPath;

}
