package dmr.submissionstore.submittable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ref {

    private String type;

    private String accession;

    private String uniqueName;
    private String teamName;

    /**
     * path to the ref within the document
     */
    private String sourceJsonPath;

}
