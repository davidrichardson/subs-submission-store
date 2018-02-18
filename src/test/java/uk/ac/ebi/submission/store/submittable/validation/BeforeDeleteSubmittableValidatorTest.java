package uk.ac.ebi.submission.store.submittable.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import uk.ac.ebi.submission.store.submittable.Submittable;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeforeDeleteSubmittableValidator.class)
public class BeforeDeleteSubmittableValidatorTest {

    @Autowired
    private BeforeDeleteSubmittableValidator validator;

    private Submittable submittable;

    @MockBean
    private CommonSubmittableValidation commonSubmittableValidation;

    @MockBean
    private Errors errors;

    @Before
    public void buildUp() {
        submittable = new Submittable();

    }

    @Test
    public void testDeleteValidation() {
        validator.validate(submittable, errors);

        Mockito.verify(commonSubmittableValidation).submissionStatusCheck(submittable, errors);
        Mockito.verify(commonSubmittableValidation).submittableStatusCheck(submittable, errors);
    }
}
