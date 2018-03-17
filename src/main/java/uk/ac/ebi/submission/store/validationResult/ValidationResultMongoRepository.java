package uk.ac.ebi.submission.store.validationResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.submission.store.security.PostAuthorizeOptionalReturnObjectHasTeamName;
import uk.ac.ebi.submission.store.security.PreAuthorizeDocumentTeamName;
import uk.ac.ebi.submission.store.security.PreAuthorizeSubmissionIdTeamName;

import java.util.Optional;

@RestResource(exported = true)
public interface ValidationResultMongoRepository extends MongoRepository<ValidationResult, String>, ValidationResultRepositoryCustom {

    @RestResource(exported = true, rel = ValidationResultSearchRelNames.BY_DOCUMENT_ID)
    ValidationResult findOneByDocumentId(@Param("documentId") String documentId);


    @RestResource(exported = true)
    @PreAuthorizeDocumentTeamName
    ValidationResult findByDocumentId(@Param("documentId") String documentId);

    @RestResource(exported = true)
    @PreAuthorizeSubmissionIdTeamName
    Page<ValidationResult> findBySubmissionId(@Param("submissionId") String submissionId, Pageable pageable);


    // exported as GET /validationresults/:id
    @Override
    @RestResource(exported = true)
    @PostAuthorizeOptionalReturnObjectHasTeamName
    Optional<ValidationResult> findById(String id);

    // controls PUT /validationresults and PATCH /validationresults/:id
    @Override
    @RestResource(exported = false)
    <S extends ValidationResult> S save(S s);

    // controls POST /validationresults
    @Override
    @RestResource(exported = false)
    <S extends ValidationResult> S insert(S s);

    // exported as DELETE /validationresults/:id
    @Override
    @RestResource(exported = false)
    void delete(ValidationResult t);

    @RestResource(exported = false)
    void deleteAllBySubmissionId(String submissionId);

}
