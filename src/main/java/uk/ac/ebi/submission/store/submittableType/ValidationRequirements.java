package uk.ac.ebi.submission.store.submittableType;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ValidationRequirements {

    /**
     * JSON schema to be used for validation of this type
     */
    @JsonRawValue
    private String schema;

    public void setSchema(JsonNode json) {
        this.schema = json.toString();
    }


    /**
     * Validators that must provide results before validation is considered complete for submittables of this type.
     * <p>
     * Assume that some validation is ordered as standard, this is just for additional validation
     */
    private ExpectedValidators expectedValidators;
}
