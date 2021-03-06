package uk.ac.ebi.submission.store.submissionDocument;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.submission.store.common.model.Audited;
import uk.ac.ebi.submission.store.common.model.Submitter;
import uk.ac.ebi.submission.store.common.model.Team;

import java.time.Instant;
import java.util.Collection;


@org.springframework.data.mongodb.core.mapping.Document
@Data
@CompoundIndexes({
        @CompoundIndex(name = "unq_subId_docType_uniqueName", def = "{'submissionId': 1, 'documentType': 1, 'uniqueName': 1}", unique = true, background = true),
        @CompoundIndex(name = "teamName_docType_uniqueName", def = "{'team.name': 1, 'documentType': 1, 'uniqueName': 1}", unique = false, background = true)
})
public class SubmissionDocument implements Identifiable<String>, Audited {

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
    private String accession;

    private String documentType;

    private ProcessingStatus status;

    @JsonRawValue
    private String content;

    public void setContent(JsonNode json) {
        this.content = json.toString();
    }


    @JsonProperty("_refs")
    private Collection<Ref> refs;

    @JsonProperty("_uploadedFileRefs")
    private Collection<UploadedFileRef> uploadedFileRefs;

    private Collection<String> checklists;



}
