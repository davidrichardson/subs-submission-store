package uk.ac.ebi.submission.store.submissionDocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ref {

    private String refType;

    private String accession;

    private String uniqueName;
    private String teamName;

    /**
     * path to the ref within the submissionDocument
     */
    private String sourceJsonPath;

}
