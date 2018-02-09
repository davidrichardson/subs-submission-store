package dmr.submissionstore.submittableType;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
public class SubmittableType {

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

    private String type;
    private String partOfType;

    @JsonRawValue
    private String schema;

    public void setSchema(JsonNode json) {
        this.schema = json.toString();
    }

    @JsonRawValue
    private String uiSchema;

    public void setUiSchema(JsonNode json) {
        this.uiSchema = json.toString();
    }


}
