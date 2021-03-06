package uk.ac.ebi.submission.store.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.submission.store.common.model.Audited;
import uk.ac.ebi.submission.store.common.model.Submitter;
import uk.ac.ebi.submission.store.common.model.Team;

import java.time.Instant;

@Document
@Data
public class Submission implements Identifiable<String>, Audited {

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


    private Submitter submitter;
    private Team team;

    private String title;
    private SubmissionStatus status;

    @JsonRawValue
    @JsonProperty("_uiData")
    private String uiData;

    public void setUiData(JsonNode json) {
        this.uiData = json.toString();
    }

}
