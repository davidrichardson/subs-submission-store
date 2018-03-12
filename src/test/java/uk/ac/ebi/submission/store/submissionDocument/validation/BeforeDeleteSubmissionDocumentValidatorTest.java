package uk.ac.ebi.submission.store.submissionDocument.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeforeDeleteSubmittableValidator.class)
public class BeforeDeleteSubmissionDocumentValidatorTest {

    @Autowired
    private BeforeDeleteSubmittableValidator validator;

    private SubmissionDocument submissionDocument;

    @MockBean
    private CommonSubmittableValidation commonSubmittableValidation;

    @MockBean
    private Errors errors;

    @Before
    public void buildUp() {
        submissionDocument = new SubmissionDocument();

    }

    @Test
    public void testDeleteValidation() {
        validator.validate(submissionDocument, errors);

        Mockito.verify(commonSubmittableValidation).submissionStatusCheck(submissionDocument, errors);
        Mockito.verify(commonSubmittableValidation).submittableStatusCheck(submissionDocument, errors);
    }
}
