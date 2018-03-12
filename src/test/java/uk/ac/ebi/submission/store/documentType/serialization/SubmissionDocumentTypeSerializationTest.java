package uk.ac.ebi.submission.store.documentType.serialization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.documentType.DisplayRequirements;
import uk.ac.ebi.submission.store.documentType.ExpectedValidators;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.documentType.ValidationRequirements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class SubmissionDocumentTypeSerializationTest {

    @Autowired
    private JacksonTester<DocumentType> json;

    @Test
    public void testSerialize() throws Exception {
        DocumentType details = submittableType();
        assertThat(this.json.write(details)).isEqualToJson("expected.documentType.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectContent<DocumentType> objectContent = this.json.read("expected.documentType.json");
        DocumentType documentType = objectContent.getObject();
        assertThat(documentType)
                .isEqualTo(submittableType());
        assertThat(documentType.getId()).isEqualTo("1234");
    }

    private DocumentType submittableType() {
        DocumentType st = new DocumentType();

        st.setTypeName("samples");
        st.setDescription("Samples of biological material");
        st.setArchiveName("BioSamples");
        st.setExampleDocument(JsonHelper.stringToJsonNode("{\"key\": \"value\"}"));
        st.setValidationRequirements(validationRequirements());
        st.setDisplayRequirements(displayRequirements());
        st.setId("1234");
        st.setCreatedBy("alice");
        st.setLastModifiedBy("bob");
        st.setVersion(10L);

        return st;
    }

    private ValidationRequirements validationRequirements() {
        ValidationRequirements vr = new ValidationRequirements();

        vr.setSchema(JsonHelper.stringToJsonNode("{\"key\": \"value\"}"));
        vr.setExpectedValidators(new ExpectedValidators());
        vr.getExpectedValidators().setRequiredValidators(
                setofStrings("taxon")
        );
        vr.getExpectedValidators().setInformationalValidators(
                setofStrings("enaSample","arrayExpressSample")
        );

        return vr;

    }

    private DisplayRequirements displayRequirements() {
        DisplayRequirements dr = new DisplayRequirements();

        dr.setUiSchema(JsonHelper.stringToJsonNode("{\"key\": \"value\"}"));
        dr.setRequiredTypes(setofStrings("something or other"));

        return dr;
    }

    private static Set<String> setofStrings(String... values){
        return new HashSet<>(
                Arrays.asList(values)
        );
    }
}
