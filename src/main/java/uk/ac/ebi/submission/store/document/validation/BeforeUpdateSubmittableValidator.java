package uk.ac.ebi.submission.store.document.validation;

import uk.ac.ebi.submission.store.document.Document;
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
        return Document.class.equals(clazz);
    }

    @NonNull
    private CommonSubmittableValidation commonSubmittableValidation;

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        Document document = (Document)target;

        logger.debug("Before update validation of document {}", document);
        logger.info("Before update validation of document {}", document.getId());

        commonSubmittableValidation.validate(document,errors);

    }
}
