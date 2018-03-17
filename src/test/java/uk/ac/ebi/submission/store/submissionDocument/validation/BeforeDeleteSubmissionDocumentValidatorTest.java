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
@SpringBootTest(classes = BeforeDeleteSubmissionDocumentValidator.class)
public class BeforeDeleteSubmissionDocumentValidatorTest {

    @Autowired
    private BeforeDeleteSubmissionDocumentValidator validator;

    private SubmissionDocument submissionDocument;

    @MockBean
    private CommonSubmissionDocumentValidation commonSubmissionDocumentValidation;

    @MockBean
    private Errors errors;

    @Before
    public void buildUp() {
        submissionDocument = new SubmissionDocument();

    }

    @Test
    public void testDeleteValidation() {
        validator.validate(submissionDocument, errors);

        Mockito.verify(commonSubmissionDocumentValidation).submissionStatusCheck(submissionDocument, errors);
        Mockito.verify(commonSubmissionDocumentValidation).submittableStatusCheck(submissionDocument, errors);
    }
}
