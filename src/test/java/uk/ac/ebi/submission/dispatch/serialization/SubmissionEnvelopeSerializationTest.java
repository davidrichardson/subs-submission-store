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
import uk.ac.ebi.submission.store.document.DocumentStatusEnum;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.submission.SubmissionStatusEnum;

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
        SubmissionEnvelope submissionEnvelope = objectContent.getObject();

        assertThat(submissionEnvelope)
                .isEqualTo(submissionEnvelope());

    }

    public SubmissionEnvelope submissionEnvelope() {
        SubmissionEnvelope se = new SubmissionEnvelope();

        se.setSubmission(submission());

        se.addSubmittedItem(document("a1","anAssay","assays"));
        se.addSubmittedItem(document("s1","aStudy","studies"));

        se.addReferencedItem(document("p1","aProject","projects"));
        se.addReferencedItem(document("sa1","aSample","samples"));

        return se;
    }

    private Document document(String id, String uniqueName, String documentType) {
        Document document = new Document();
        document.setId(id);
        document.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        document.setTeam(Team.of("subs.testTeam"));
        document.setUniqueName(uniqueName);
        document.setDocumentType(documentType);
        document.setStatus(DocumentStatusEnum.Completed);
        document.setContent(JsonHelper.stringToJsonNode("{\"key\": \"value\"}"));
        return document;
    }

    private Submission submission() {
        Submission submission = new Submission();
        submission.setId("5556");
        submission.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        submission.setTeam(Team.of("subs.testTeam"));
        submission.setTitle("A wonderful test submission");
        submission.setStatus(SubmissionStatusEnum.Submitted);
        return submission;
    }
}
