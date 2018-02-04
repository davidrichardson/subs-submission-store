package dmr.submissionstore.submittable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadedFileRef {

    private String uploadedFile;

    /**
     * Path to the file ref within the document
     */
    private String sourceJsonPath;
}
