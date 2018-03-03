package uk.ac.ebi.submission.store.submittable;

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
public class SubmittableResource extends Resource<Submittable> {

    public SubmittableResource(Resource<Submittable> resource) {
        super(resource.getContent(), resource.getLinks());
        this.auditDetails = new AuditDetails(resource.getContent());
    }

    @JsonProperty("_actions")
    private final SubmittableResource.Actions actions = new SubmittableResource.Actions();

    @JsonProperty("_audit")
    private final AuditDetails auditDetails;

    @Data
    public static class Actions {
        private boolean updateable;
        private boolean deleteable;
    }
}
