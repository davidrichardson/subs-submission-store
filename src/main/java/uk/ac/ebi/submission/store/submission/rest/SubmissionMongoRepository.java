package uk.ac.ebi.submission.store.submission.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.submission.store.security.PostAuthorizeOptionalReturnObjectHasTeamName;
import uk.ac.ebi.submission.store.security.PreAuthorizeParamTeamName;
import uk.ac.ebi.submission.store.security.PreAuthorizeSubmissionTeamName;
import uk.ac.ebi.submission.store.submission.Submission;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = true)
public interface SubmissionMongoRepository extends MongoRepository<Submission,String>{

    // exported as GET /things/:id
    @RestResource(exported = true)
    @PostAuthorizeOptionalReturnObjectHasTeamName
    Optional<Submission> findById(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = true)
    Page<Submission> findAll(Pageable pageable);

    // Prevents PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmissionTeamName
    <S extends Submission> S save(@P("entity") S entity);

    // Prevents POST /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmissionTeamName
    <S extends Submission> S insert(@P("entity") S entity);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmissionTeamName
    void delete(@P("entity") Submission entity);

    @RestResource(exported = true, rel = SubmissionSearchRelNames.TEAM_NAME)
    @PreAuthorizeParamTeamName
    Page<Submission> findByTeamName(@Param(value = "teamName") String teamName, Pageable pageable);

    @RestResource(exported = false)
    Page<Submission> findByTeamNameInOrderByCreatedByDesc(List<String> teamNames, Pageable pageable);






}
