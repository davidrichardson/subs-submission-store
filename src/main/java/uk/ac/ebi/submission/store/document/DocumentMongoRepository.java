package uk.ac.ebi.submission.store.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//TODO read/write by owner or admin
@RepositoryRestResource
public interface DocumentMongoRepository extends MongoRepository<Document, String> {

    Document findOneBySubmissionIdAndUniqueNameAndDocumentType(String submissionId, String uniqueName, String documentType);

    default Document findOneBySubmissionIdAndUniqueNameAndDocumentType(Document exampleDocument) {
        return this.findOneBySubmissionIdAndUniqueNameAndDocumentType(
                exampleDocument.getSubmissionId(),
                exampleDocument.getUniqueName(),
                exampleDocument.getDocumentType()
        );
    }
}
