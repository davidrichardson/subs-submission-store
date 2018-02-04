package dmr.submissionstore.submittable.validation;

import dmr.submissionstore.submittable.Submittable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeDeleteSubmittableValidator")
@Slf4j
public class BeforeDeleteSubmittableValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Submittable.class.equals(clazz);
    }

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        Submittable submittable = (Submittable)target;

        log.debug("Before delete validation of submittable {}",submittable);
        log.info("Before delete validation of submittable {}",submittable.getId());


        //TODO submission is open
        //TODO submittable is open
    }
}
