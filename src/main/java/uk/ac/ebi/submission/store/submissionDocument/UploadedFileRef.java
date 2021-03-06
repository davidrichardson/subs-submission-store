package uk.ac.ebi.submission.store.submissionDocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadedFileRef {

    private String uploadedFile;

    /**
     * Path to the file ref within the submissionDocument
     */
    private String sourceJsonPath;
}
