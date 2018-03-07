package uk.ac.ebi.submission.store.documentType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DocumentTypeMongoRepository extends MongoRepository<DocumentType, String> {

    Optional<DocumentType> findByTypeName(@Param("typeName") String typeName);
}
