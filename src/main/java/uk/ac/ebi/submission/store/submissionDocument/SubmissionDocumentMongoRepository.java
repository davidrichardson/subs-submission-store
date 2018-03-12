package uk.ac.ebi.submission.store.submissionDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.submission.store.security.*;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface SubmissionDocumentMongoRepository extends MongoRepository<SubmissionDocument, String>, SubmissionDocumentRepositoryCustom {

    // exported as GET /things/:id
    @RestResource(exported = true)
    @PostAuthorizeOptionalReturnObjectHasTeamName
    Optional<SubmissionDocument> findById(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = true)
    Page<SubmissionDocument> findAll(Pageable pageable);

    // controls PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    <S extends SubmissionDocument> S save(@P("entity") S entity);

    // controls POST /things
    @Override
    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    <S extends SubmissionDocument> S insert(@P("entity") S s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    void delete(@P("entity") SubmissionDocument entity);


    @RestResource(exported = false)
    List<SubmissionDocument> findBySubmissionId(String submissionId);

    @RestResource(exported = true)
    @PreAuthorizeSubmissionIdTeamName
    Page<SubmissionDocument> findBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId, Pageable pageable);


    @RestResource(exported = false)
    void deleteBySubmissionId(String submissionId);

    @RestResource(exported = true)
    SubmissionDocument findOneBySubmissionIdAndUniqueNameAndDocumentType(String submissionId, String uniqueName, String documentType);

    default SubmissionDocument findOneBySubmissionIdAndUniqueNameAndDocumentType(SubmissionDocument exampleSubmissionDocument) {
        return this.findOneBySubmissionIdAndUniqueNameAndDocumentType(
                exampleSubmissionDocument.getSubmissionId(),
                exampleSubmissionDocument.getUniqueName(),
                exampleSubmissionDocument.getDocumentType()
        );
    }
}
