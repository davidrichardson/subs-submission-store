package dmr.submissionstore.submittable.validation;

import dmr.submissionstore.submittable.Submittable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeUpdateSubmittableValidator")
public class BeforeUpdateSubmittableValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return Submittable.class.equals(clazz);
    }

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        Submittable submittable = (Submittable)target;

        logger.debug("Before update validation of submittable {}",submittable);
        logger.info("Before update validation of submittable {}",submittable.getId());


        //TODO unique name has not changed

        //TODO submission has not changed provided
        //TODO submission is open

        //TODO submittable record is open

        //TODO validate document is valid JSON
    }
}
