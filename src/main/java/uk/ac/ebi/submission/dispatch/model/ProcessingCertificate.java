package uk.ac.ebi.submission.dispatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingCertificate {

    private String submittableId;
    private String archive;
    private String processingStatus;
    private String accession;
    private String message;
}
