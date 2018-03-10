package uk.ac.ebi.submission.store.documentType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource
public interface DocumentTypeMongoRepository extends MongoRepository<DocumentType, String> {

    @RestResource(rel = "typeName")
    Optional<DocumentType> findByTypeName(@Param("typeName") String typeName);
}
