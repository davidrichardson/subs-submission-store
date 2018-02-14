package dmr.submissionstore.submittable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadedFileRef {

    private String uploadedFile;

    /**
     * Path to the file ref within the document
     */
    private String sourceJsonPath;
}
