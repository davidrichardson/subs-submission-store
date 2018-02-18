package uk.ac.ebi.submission.store.validationResult;

import org.springframework.data.mongodb.repository.MongoRepository;


//TODO read by owner or admin, write by admin
public interface ValidationResultMongoRepository extends MongoRepository<ValidationResult,String>{

}
