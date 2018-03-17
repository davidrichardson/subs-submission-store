package uk.ac.ebi.submission.store.submission.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.ac.ebi.submission.store.submission.Submission;

@Component("beforeCreateSubmissionvalidator")
@RequiredArgsConstructor
@Slf4j
public class BeforeCreateSubmissionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Submission.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
