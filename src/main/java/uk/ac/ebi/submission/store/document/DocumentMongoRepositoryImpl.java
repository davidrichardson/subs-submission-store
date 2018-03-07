package uk.ac.ebi.submission.store.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public class DocumentMongoRepositoryImpl {

    public Page<Document> findAll(Pageable pageable) {
        return new PageImpl<Document>(Collections.emptyList());
    }
}
