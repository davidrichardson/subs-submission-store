package uk.ac.ebi.submission.store.submissionDocument.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;

import java.util.Collections;

public class SubmissionDocumentMongoRepositoryImpl {

    public Page<SubmissionDocument> findAll(Pageable pageable) {
        return new PageImpl<SubmissionDocument>(Collections.emptyList());
    }
}
