package uk.ac.ebi.submission.store.submission.serialization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.JsonHelper;
import uk.ac.ebi.submission.store.common.model.Submitter;
import uk.ac.ebi.submission.store.common.model.Team;
import uk.ac.ebi.submission.store.submission.Submission;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class SubmissionSerializationTest {

    @Autowired
    private JacksonTester<Submission> json;

    @Test
    public void testSerialize() throws Exception {
        Submission details = submission();
        assertThat(this.json.write(details)).isEqualToJson("expected.submission.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectContent<Submission> submissionObjectContent = this.json.read("expected.submission.json");
        Submission submission = submissionObjectContent.getObject();
        assertThat(submission)
                .isEqualTo(submission());
        assertThat(submission.getId()).isEqualTo("5556");
    }


    private Submission submission() {
        Submission s = new Submission();
        s.setId("5556");
        s.setCreatedBy("alice");
        s.setLastModifiedBy("bob");
        s.setVersion(1L);
        s.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        s.setTeam(Team.of("subs.testTeam"));
        s.setTitle("A wonderful test submission");
        s.setStatus("Draft");
        s.setUiData(JsonHelper.stringToJsonNode("[{\"a\": \"b\"},\"could be anything\"]"));
        return s;
    }
}



