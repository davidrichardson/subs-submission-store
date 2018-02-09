package dmr.submissionstore.submission;

import org.springframework.data.mongodb.repository.MongoRepository;


//TODO read/write by owner or admin
public interface SubmissionMongoRepository extends MongoRepository<Submission,String>{
}
