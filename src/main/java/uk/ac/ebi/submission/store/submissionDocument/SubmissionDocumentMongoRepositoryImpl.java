package uk.ac.ebi.submission.store.submissionDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public class SubmissionDocumentMongoRepositoryImpl {

    public Page<SubmissionDocument> findAll(Pageable pageable) {
        return new PageImpl<SubmissionDocument>(Collections.emptyList());
    }
}
