package dmr.submissionstore.validationResult;

import dmr.submissionstore.submission.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;


//TODO read by owner or admin, write by admin
public interface ValidationResultMongoRepository extends MongoRepository<ValidationResult,String>{

}
