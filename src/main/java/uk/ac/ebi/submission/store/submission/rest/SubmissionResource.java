package uk.ac.ebi.submission.store.submission.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.Resource;
import uk.ac.ebi.submission.store.common.model.AuditDetails;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionStatus;

import java.util.Set;
import java.util.TreeSet;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class SubmissionResource extends Resource<Submission> {

    public SubmissionResource(Resource<Submission> resource){
        super(resource.getContent(),resource.getLinks());
        this.auditDetails = new AuditDetails(resource.getContent());
    }

    @JsonProperty("_actions")
    private final Actions actions = new Actions();

    @JsonProperty("_audit")
    private final AuditDetails auditDetails;

    @Data
    public static class Actions {
        private boolean updateable;
        private boolean deleteable;
        private final Set<SubmissionStatus> statuses = new TreeSet<>();
    }

}
