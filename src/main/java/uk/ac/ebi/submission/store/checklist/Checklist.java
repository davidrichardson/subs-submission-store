package uk.ac.ebi.submission.store.checklist;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Document
@CompoundIndexes({
        @CompoundIndex(name = "submittableType_tags", def = "{'documentType': 1, 'tags': 1}", background = true),
        @CompoundIndex(name = "submittableType_name", def = "{'documentType': 1, 'name': 1}", unique = true, background = true)
})
public class Checklist {


    /**
     * UI schema to be used to drive display of this checklist
     */
    @JsonRawValue
    private String uiSchema;

    public void setUiSchema(JsonNode json) {
        this.uiSchema = json.toString();
    }

    /**
     * JSON schema to be used for validation of this checklist
     */
    @JsonRawValue
    private String schema;

    public void setSchema(JsonNode json) {
        this.schema = json.toString();
    }


    /**
     * Tags to describe this checklist
     */

    private Set<String> tags = new HashSet<>();

    /**
     * Document type that this checklist is relevant to
     */
    private String submittableType;

    private String name;

    private String description;


    @Id
    private String id;

    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant lastModifiedAt;
    @Version
    private Long version;
}
