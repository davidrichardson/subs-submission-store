package dmr.submissionstore.submittable;

import org.springframework.data.mongodb.repository.MongoRepository;

//TODO read/write by owner or admin
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
