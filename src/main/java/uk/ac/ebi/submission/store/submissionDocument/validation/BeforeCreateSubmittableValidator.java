package uk.ac.ebi.submission.store.submissionDocument.validation;

import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocumentMongoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateSubmittableValidator")
@RequiredArgsConstructor
@Slf4j
public class BeforeCreateSubmittableValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SubmissionDocument.class.equals(clazz);
    }

    @NonNull
    private SubmissionDocumentMongoRepository submissionDocumentMongoRepository;

    @NonNull
    private CommonSubmittableValidation commonSubmittableValidation;

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        SubmissionDocument submissionDocument = (SubmissionDocument) target;

        log.debug("Before create validation of submissionDocument {}", submissionDocument);
        log.info("Before create validation of submissionDocument {}", submissionDocument.getId());

        commonSubmittableValidation.validate(submissionDocument, errors);

        //TODO if alias exists for refType + team, ensure consistent with existing record (refType, alias)
        //TODO check that there's at mose only one incomplete version of the record being edited
    }
}
