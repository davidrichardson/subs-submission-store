package dmr.submissionstore.submittable;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmittableMongoRepository extends MongoRepository<Submittable, String> {
}
