package uk.ac.ebi.submission.store.submittable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.submission.store.common.model.Submitter;
import uk.ac.ebi.submission.store.common.model.Team;

import java.time.Instant;
import java.util.Collection;


@Document
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@CompoundIndexes({
        @CompoundIndex(name = "unq_subId_docType_uniqueName", def = "{'submissionId': 1, 'documentType': 1, 'uniqueName': 1}", unique = true),
        @CompoundIndex(name = "teamName_docType_uniqueName", def = "{'team.name': 1, 'documentType': 1, 'uniqueName': 1}", unique = false)
})
public class Submittable implements Identifiable<String> {

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

    private String submissionId;
    private String uniqueName;

    private String documentType;
    private String submittableTypeId;

    private String status;

    private String validationStatus;

    @JsonRawValue
    private String document;

    public void setDocument(JsonNode json) {
        this.document = json.toString();
    }


    private Collection<Ref> refs;
    private Collection<UploadedFileRef> uploadedFileRefs;


}
