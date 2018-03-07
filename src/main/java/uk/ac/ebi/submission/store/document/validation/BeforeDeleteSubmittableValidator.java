package uk.ac.ebi.submission.store.document.validation;

import uk.ac.ebi.submission.store.document.Document;
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
        return Document.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Document document = (Document) target;

        log.debug("Before delete validation of document {}", document);
        log.info("Before delete validation of document {}", document.getId());

        commonSubmittableValidation.submissionStatusCheck(document, errors);
        commonSubmittableValidation.submittableStatusCheck(document, errors);
    }
}
