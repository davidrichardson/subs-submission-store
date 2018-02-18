package uk.ac.ebi.submission.store.submittableType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmittableTypeMongoRepository extends MongoRepository<SubmittableType, String> {

    Optional<SubmittableType> findByTypeName(@Param("typeName") String typeName);
}
