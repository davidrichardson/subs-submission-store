package uk.ac.ebi.submission.store.submissionDocument.validation;

import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeUpdateSubmittableValidator")
@RequiredArgsConstructor
public class BeforeUpdateSubmittableValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return SubmissionDocument.class.equals(clazz);
    }

    @NonNull
    private CommonSubmittableValidation commonSubmittableValidation;

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        SubmissionDocument submissionDocument = (SubmissionDocument)target;

        logger.debug("Before update validation of submissionDocument {}", submissionDocument);
        logger.info("Before update validation of submissionDocument {}", submissionDocument.getId());

        commonSubmittableValidation.validate(submissionDocument,errors);

    }
}
