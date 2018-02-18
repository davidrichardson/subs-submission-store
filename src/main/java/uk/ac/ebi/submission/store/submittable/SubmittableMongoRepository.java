package uk.ac.ebi.submission.store.submittable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//TODO read/write by owner or admin
@RepositoryRestResource
public interface SubmittableMongoRepository extends MongoRepository<Submittable, String> {

    Submittable findOneBySubmissionIdAndUniqueNameAndDocumentType(String submissionId, String uniqueName, String documentType);

    default Submittable findOneBySubmissionIdAndUniqueNameAndDocumentType(Submittable exampleSubmittable) {
        return this.findOneBySubmissionIdAndUniqueNameAndDocumentType(
                exampleSubmittable.getSubmissionId(),
                exampleSubmittable.getUniqueName(),
                exampleSubmittable.getDocumentType()
        );
    }
}
