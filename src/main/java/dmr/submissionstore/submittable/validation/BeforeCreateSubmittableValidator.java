package dmr.submissionstore.submittable.validation;

import dmr.submissionstore.submittable.Submittable;
import dmr.submissionstore.submittable.SubmittableMongoRepository;
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
        return Submittable.class.equals(clazz);
    }

    @NonNull
    private SubmittableMongoRepository submittableMongoRepository;

    @NonNull
    private CommonSubmittableValidation commonSubmittableValidation;

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        Submittable submittable = (Submittable) target;

        log.debug("Before create validation of submittable {}", submittable);
        log.info("Before create validation of submittable {}", submittable.getId());

        commonSubmittableValidation.validate(submittable, errors);

        //TODO if alias exists for refType + team, ensure consistent with existing record (refType, alias)
        //TODO check that there's at mose only one incomplete version of the record being edited
    }
}
