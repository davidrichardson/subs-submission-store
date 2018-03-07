package uk.ac.ebi.submission.store.documentType;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.time.Instant;

@Document
@Data
public class DocumentType implements Identifiable<String> {

    /**
     * plural name for this type. e.g. samples
     */
    @Indexed(unique = true, background = true)
    private String typeName;

    /**
     * Description of what this type is for, to be presented to users
     */
    private String description;

    /**
     * Example document of this type, that we can present to users to help them understand how to use this type
     */
    @JsonRawValue
    private String exampleDocument;

    public void setExampleDocument(JsonNode json){
        this.exampleDocument = json.toString();
    }


    /**
     * name of the archive that accepts this type
     */
    private String archiveName;

    private ValidationRequirements validationRequirements;
    private DisplayRequirements displayRequirements;

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


}
