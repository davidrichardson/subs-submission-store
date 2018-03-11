package uk.ac.ebi.submission.store.documentType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(exported = true)
public interface DocumentTypeMongoRepository extends MongoRepository<DocumentType, String> {

    @RestResource(exported = true, rel = DocumentTypeSearchRelNames.FIND_ONE_BY_NAME)
    Optional<DocumentType> findOneByTypeName(@Param("typeName") String typeName);
}
