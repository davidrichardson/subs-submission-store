package uk.ac.ebi.submission.store.validationResult;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;


//TODO read by owner or admin, write by admin
public interface ValidationResultMongoRepository extends MongoRepository<ValidationResult, String> {

    @RestResource(exported = true, rel = ValidationResultRelNames.BY_SUBMITTABLE_ID)
    ValidationResult findOneBySubmittableId(@Param("submittableId") String submittableId);

}
