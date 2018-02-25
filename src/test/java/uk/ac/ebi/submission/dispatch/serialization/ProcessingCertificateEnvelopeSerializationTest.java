package uk.ac.ebi.submission.dispatch.serialization;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.dispatch.model.ProcessingCertificate;
import uk.ac.ebi.submission.dispatch.model.ProcessingCertificateEnvelope;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class ProcessingCertificateEnvelopeSerializationTest {

    @Autowired
    private JacksonTester<ProcessingCertificateEnvelope> json;

    @Test
    public void testSerialize() throws Exception {
        ProcessingCertificateEnvelope details = processingCertificateEnvelope();
        assertThat(this.json.write(details)).isEqualToJson("expected.processingCertificateEnvelope.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectContent<ProcessingCertificateEnvelope> objectContent = this.json.read("expected.processingCertificateEnvelope.json");
        ProcessingCertificateEnvelope processingCertificateEnvelope = objectContent.getObject();
        ProcessingCertificateEnvelope expected = processingCertificateEnvelope();

        assertThat(processingCertificateEnvelope)
                .isEqualTo(processingCertificateEnvelope());

    }

    public ProcessingCertificateEnvelope processingCertificateEnvelope() {
        ProcessingCertificateEnvelope pce = new ProcessingCertificateEnvelope();

        pce.setSubmissionId("s12345");

        pce.addProcessingCertificate(new ProcessingCertificate("dABC", "MY-ARCHIVE", "Completed", "acc789", null));
        pce.addProcessingCertificate(new ProcessingCertificate("dDEF", "MY-ARCHIVE", "Errored", null, "broken"));

        return pce;
    }
}
