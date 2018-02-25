package uk.ac.ebi.submission.dispatch.serialization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.dispatch.model.SubmissionEnvelope;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.model.Submitter;
import uk.ac.ebi.submission.store.common.model.Team;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittable.Submittable;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class SubmissionEnvelopeSerializationTest {

    @Autowired
    private JacksonTester<SubmissionEnvelope> json;

    @Test
    public void testSerialize() throws Exception {
        SubmissionEnvelope details = submissionEnvelope();
        assertThat(this.json.write(details)).isEqualToJson("expected.submissionEnvelope.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectContent<SubmissionEnvelope> objectContent = this.json.read("expected.submissionEnvelope.json");
        SubmissionEnvelope processingCertificateEnvelope = objectContent.getObject();

        assertThat(processingCertificateEnvelope)
                .isEqualTo(submissionEnvelope());

    }

    public SubmissionEnvelope submissionEnvelope() {
        SubmissionEnvelope se = new SubmissionEnvelope();

        se.setSubmission(submission());

        se.addSubmittedItem(submittable("a1","anAssay","assays"));
        se.addSubmittedItem(submittable("s1","aStudy","studies"));

        se.addReferencedItem(submittable("p1","aProject","projects"));
        se.addReferencedItem(submittable("sa1","aSample","samples"));

        return se;
    }

    private Submittable submittable(String id, String uniqueName, String documentType) {
        Submittable submittable = new Submittable();
        submittable.setId(id);
        submittable.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        submittable.setTeam(Team.of("subs.testTeam"));
        submittable.setUniqueName(uniqueName);
        submittable.setDocumentType(documentType);
        submittable.setStatus("a status");
        submittable.setDocument(JsonHelper.stringToJsonNode("{\"key\": \"value\"}"));
        return submittable;
    }

    private Submission submission() {
        Submission submission = new Submission();
        submission.setId("5556");
        submission.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        submission.setTeam(Team.of("subs.testTeam"));
        submission.setTitle("A wonderful test submission");
        submission.setStatus("Submitted");
        return submission;
    }
}
