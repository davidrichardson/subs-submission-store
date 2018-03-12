package uk.ac.ebi.submission.store.submissionDocument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.Resource;
import uk.ac.ebi.submission.store.common.model.AuditDetails;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class SubmissionDocumentResource extends Resource<SubmissionDocument> {

    public SubmissionDocumentResource(Resource<SubmissionDocument> resource) {
        super(resource.getContent(), resource.getLinks());
        this.auditDetails = new AuditDetails(resource.getContent());
    }

    @JsonProperty("_actions")
    private final SubmissionDocumentResource.Actions actions = new SubmissionDocumentResource.Actions();

    @JsonProperty("_audit")
    private final AuditDetails auditDetails;

    @Data
    public static class Actions {
        private boolean updateable;
        private boolean deleteable;
    }
}
