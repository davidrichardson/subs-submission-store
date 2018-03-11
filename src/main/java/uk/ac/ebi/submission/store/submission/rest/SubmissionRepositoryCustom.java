package uk.ac.ebi.submission.store.submission.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.submission.store.submission.Submission;

public interface SubmissionRepositoryCustom {

    Page<Submission> findAll(Pageable pageable);
}
