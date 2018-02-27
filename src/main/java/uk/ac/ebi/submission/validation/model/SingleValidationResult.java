package uk.ac.ebi.submission.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleValidationResult {

    private String validationStatus;
    private String message;
    private String entityUuid;
    private String jsonPath;
}
