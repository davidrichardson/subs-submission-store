package dmr.submissionstore.submission;

import dmr.submissionstore.common.model.Submitter;
import dmr.submissionstore.common.model.Team;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.time.Instant;

@Document
@Data
public class Submission implements Identifiable<String> {

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
    private String status;

}
