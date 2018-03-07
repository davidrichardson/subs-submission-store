package uk.ac.ebi.submission.store.document.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import uk.ac.ebi.submission.store.document.Document;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeforeDeleteSubmittableValidator.class)
public class BeforeDeleteDocumentValidatorTest {

    @Autowired
    private BeforeDeleteSubmittableValidator validator;

    private Document document;

    @MockBean
    private CommonSubmittableValidation commonSubmittableValidation;

    @MockBean
    private Errors errors;

    @Before
    public void buildUp() {
        document = new Document();

    }

    @Test
    public void testDeleteValidation() {
        validator.validate(document, errors);

        Mockito.verify(commonSubmittableValidation).submissionStatusCheck(document, errors);
        Mockito.verify(commonSubmittableValidation).submittableStatusCheck(document, errors);
    }
}
