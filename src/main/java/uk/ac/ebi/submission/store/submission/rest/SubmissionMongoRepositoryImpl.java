package uk.ac.ebi.submission.store.submission.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.submission.store.submission.Submission;

import java.util.Collections;

public class SubmissionMongoRepositoryImpl {

    public Page<Submission> findAll(Pageable pageable) {
        return new PageImpl<Submission>(Collections.emptyList());
    }


}
