package dmr.submissionstore.submittable.validation;

import dmr.submissionstore.submittable.Submittable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateSubmittableValidator")
@Slf4j
public class BeforeCreateSubmittableValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Submittable.class.equals(clazz);
    }

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        Submittable submittable = (Submittable)target;

        log.debug("Before create validation of submittable {}",submittable);
        log.info("Before create validation of submittable {}",submittable.getId());

        //TODO unique name is provided


        //TODO submission is provided
        //TODO submission is found
        //TODO submission is open

        //TODO unique name is unique within submission / type
        //TODO if alias exists for type + team, ensure consistent with existing record (type, alias)


        //TODO validate document is valid JSON
    }
}
