package uk.ac.ebi.submission.store.validationResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public class ValidationResultMongoRepositoryImpl {

    public Page<ValidationResult> findAll(Pageable pageable) {
        return new PageImpl<ValidationResult>(Collections.emptyList());
    }
}
