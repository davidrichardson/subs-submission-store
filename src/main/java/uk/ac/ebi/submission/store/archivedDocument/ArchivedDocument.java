package uk.ac.ebi.submission.store.archivedDocument;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.*;
import uk.ac.ebi.submission.store.common.model.Submitter;
import uk.ac.ebi.submission.store.common.model.Team;
import uk.ac.ebi.submission.store.document.DocumentStatusEnum;
import uk.ac.ebi.submission.store.document.Ref;
import uk.ac.ebi.submission.store.document.UploadedFileRef;

import java.time.Instant;
import java.util.Collection;

public class ArchivedDocument {

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

    private String uniqueName;
    private String accession;


    private DocumentStatusEnum status;

    @JsonRawValue
    private String content;

    public void setContent(JsonNode json) {
        this.content = json.toString();
    }


    private Collection<Ref> refs;
    private Collection<UploadedFileRef> uploadedFileRefs;

    private Collection<String> checklists;


}
