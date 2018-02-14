package dmr.submissionstore.submittable;

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
     * path to the ref within the document
     */
    private String sourceJsonPath;

}
