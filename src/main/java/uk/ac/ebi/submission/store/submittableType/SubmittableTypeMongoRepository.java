package uk.ac.ebi.submission.store.submittableType;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubmittableTypeMongoRepository extends MongoRepository<SubmittableType, String> {

    Optional<SubmittableType> findByTypeName(String typeName);
}
