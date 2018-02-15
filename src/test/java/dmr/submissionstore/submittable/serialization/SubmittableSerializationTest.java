package dmr.submissionstore.submittable.serialization;

import dmr.submissionstore.JsonHelper;
import dmr.submissionstore.common.model.Submitter;
import dmr.submissionstore.common.model.Team;
import dmr.submissionstore.submittable.Ref;
import dmr.submissionstore.submittable.Submittable;
import dmr.submissionstore.submittable.UploadedFileRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class SubmittableSerializationTest {

    @Autowired
    private JacksonTester<Submittable> json;

    @Test
    public void testSerialize() throws Exception {
        Submittable details = submittable();
        assertThat(this.json.write(details)).isEqualToJson("expected-submittable.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectContent<Submittable> submittableObjectContent = this.json.read("expected-submittable.json");
        Submittable submittable = submittableObjectContent.getObject();
        assertThat(submittable)
                .isEqualTo(submittable());
        assertThat(submittable.getId()).isEqualTo("1234");
    }

    private Submittable submittable() {
        Submittable s = new Submittable();
        s.setId("1234");
        s.setCreatedBy("Alice");
        s.setLastModifiedBy("Bob");
        s.setVersion(1L);
        s.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        s.setTeam(Team.of("subs.testTeam"));
        s.setSubmissionId("s456");
        s.setUniqueName("my-sample");
        s.setDocumentType("samples");
        s.setStatus("Draft");
        s.setDocument(JsonHelper.stringToJsonNode("[{\"a\": \"b\"},\"could be anything\"]"));
        s.setRefs(Arrays.asList(
                ref()
        ));
        s.setUploadedFileRefs(Arrays.asList(
                uploadedFileRef()
        ));
        return s;
    }

    private Ref ref() {
        Ref r = new Ref();
        r.setAccession("a1");
        r.setSourceJsonPath("$.['ref']");
        r.setTeamName("self.testTeam");
        r.setUniqueName("un1");
        r.setRefType("samples");
        return r;
    }

    private UploadedFileRef uploadedFileRef() {
        UploadedFileRef u = new UploadedFileRef();
        u.setSourceJsonPath("$.['file']");
        u.setUploadedFile("test.txt");
        return u;
    }

}




