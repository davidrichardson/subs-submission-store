package uk.ac.ebi.submission.store.submission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public class SubmissionMongoRepositoryImpl {

    public Page<Submission> findAll(Pageable pageable) {
        return new PageImpl<Submission>(Collections.emptyList());
    }
}
