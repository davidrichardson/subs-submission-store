package uk.ac.ebi.submission.store.submissionplanwizard;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class SubmissionPlan {

    private String displayName;

    private String description;

    private Set<String> submittableTypes = new LinkedHashSet<>();


}
