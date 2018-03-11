package uk.ac.ebi.submission.store.submission.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.submission.store.submission.Submission;

import java.util.List;
import java.util.Map;

public interface SubmissionRepositoryCustom {

    Page<Submission> findAll(Pageable pageable);

    Map<String,Integer> statusCountsByTeam(List<String> teamNames);
}
