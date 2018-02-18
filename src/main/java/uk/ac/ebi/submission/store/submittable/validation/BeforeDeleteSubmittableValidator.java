package uk.ac.ebi.submission.store.submittable.validation;

import uk.ac.ebi.submission.store.submittable.Submittable;
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
        return Submittable.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Submittable submittable = (Submittable) target;

        log.debug("Before delete validation of submittable {}", submittable);
        log.info("Before delete validation of submittable {}", submittable.getId());

        commonSubmittableValidation.submissionStatusCheck(submittable, errors);
        commonSubmittableValidation.submittableStatusCheck(submittable, errors);
    }
}
