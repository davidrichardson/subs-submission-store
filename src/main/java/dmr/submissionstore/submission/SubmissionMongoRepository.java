package dmr.submissionstore.submission;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmissionMongoRepository extends MongoRepository<Submission,String>{
}
