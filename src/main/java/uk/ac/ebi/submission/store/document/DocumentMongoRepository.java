package uk.ac.ebi.submission.store.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.submission.store.security.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface DocumentMongoRepository extends MongoRepository<Document, String>, DocumentRepositoryCustom {

    // exported as GET /things/:id
    @RestResource(exported = true)
    @PostAuthorizeOptionalReturnObjectHasTeamName
    Optional<Document> findById(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = true)
    Page<Document> findAll(Pageable pageable);

    // controls PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    <S extends Document> S save(@P("entity") S entity);

    // controls POST /things
    @Override
    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    <S extends Document> S insert(@P("entity") S s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    void delete(@P("entity") Document entity);


    @RestResource(exported = false)
    List<Document> findBySubmissionId(String submissionId);

    @RestResource(exported = true)
    @PreAuthorizeSubmissionIdTeamName
    Page<Document> findBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId, Pageable pageable);


    @RestResource(exported = false)
    void deleteBySubmissionId(String submissionId);

    @RestResource(exported = true)
    Document findOneBySubmissionIdAndUniqueNameAndDocumentType(String submissionId, String uniqueName, String documentType);

    default Document findOneBySubmissionIdAndUniqueNameAndDocumentType(Document exampleDocument) {
        return this.findOneBySubmissionIdAndUniqueNameAndDocumentType(
                exampleDocument.getSubmissionId(),
                exampleDocument.getUniqueName(),
                exampleDocument.getDocumentType()
        );
    }
}
