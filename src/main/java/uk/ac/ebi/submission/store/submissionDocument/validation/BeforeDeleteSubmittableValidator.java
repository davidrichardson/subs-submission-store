package uk.ac.ebi.submission.store.submissionDocument.validation;

import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeDeleteSubmittableValidator")
@RequiredArgsConstructor
@Slf4j
public class BeforeDeleteSubmittableValidator implements Validator {

    @NonNull
    private CommonSubmittableValidation commonSubmittableValidation;

    @Override
    public boolean supports(Class<?> clazz) {
        return SubmissionDocument.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SubmissionDocument submissionDocument = (SubmissionDocument) target;

        log.debug("Before delete validation of submissionDocument {}", submissionDocument);
        log.info("Before delete validation of submissionDocument {}", submissionDocument.getId());

        commonSubmittableValidation.submissionStatusCheck(submissionDocument, errors);
        commonSubmittableValidation.submittableStatusCheck(submissionDocument, errors);
    }
}
