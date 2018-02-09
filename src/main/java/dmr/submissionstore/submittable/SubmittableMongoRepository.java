package dmr.submissionstore.submittable;

import org.springframework.data.mongodb.repository.MongoRepository;

//TODO read/write by owner or admin
public interface SubmittableMongoRepository extends MongoRepository<Submittable, String> {
}
