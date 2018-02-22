package uk.ac.ebi.submission.store.submittableType;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.Set;

@Data
public class DisplayRequirements {

    private String displayName;

    /**
     * UI schema to be used to drive display of this type
     */
    @JsonRawValue
    private String uiSchema;

    public void setUiSchema(JsonNode json) {
        this.uiSchema = json.toString();
    }

    /**
     * names of the types that are required in a submission for this type to be worth displaying in a UI. All elements of
     * the set must be present before this type is considered relevant
     * <p>
     * e.g. ENA study requires a Project, so don't display the study interface until a project is added to the submission
     */
    private Set<String> requiredTypes;
}
