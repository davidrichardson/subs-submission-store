package dmr.submissionstore.submittableType;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.time.Instant;

@Document
@Data
public class SubmittableType implements Identifiable<String> {

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

    @Indexed(unique = true)
    private String typeName;

    @JsonRawValue
    private String schema;

    public void setSchema(JsonNode json) {
        this.schema = json.toString();
    }

    @JsonRawValue
    private String uiSchema;
    public void setUiSchema(JsonNode json) { this.uiSchema = json.toString(); }

}
