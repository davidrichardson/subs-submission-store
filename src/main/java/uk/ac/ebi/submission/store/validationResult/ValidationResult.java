package uk.ac.ebi.submission.store.validationResult;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Data
public class ValidationResult {

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
    private String submittableId;

    @Indexed
    private String submissionId;

    private String submittableVersion;

    private boolean passedValidation;

    private Collection<SingleValidationResult> results;

    private Map<String,Boolean> resultsReceived = new HashMap<>();

    private Map<String, List<SingleValidationResult>> expectedResults = new HashMap<>();

}
