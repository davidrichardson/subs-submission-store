package dmr.submissionstore.submittableType;

import dmr.submissionstore.submission.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;


//TODO read without authentication or admin
public interface SubmittableTypeMongoRepository extends MongoRepository<SubmittableType,String>{
}
