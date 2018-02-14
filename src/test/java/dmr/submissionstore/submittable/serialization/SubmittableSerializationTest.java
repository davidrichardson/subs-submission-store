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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@JsonTest
public class SubmittableSerializationTest {

    @Autowired
    private JacksonTester<Submittable> json;


    @Test
    public void testSerialize() throws Exception {
        Submittable details = submittable();
        assertThat(this.json.write(details)).isEqualToJson(json());
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = json();
        assertThat(this.json.parse(json()))
                .isEqualTo(submittable());
        assertThat(this.json.parseObject(json).getId()).isEqualTo("1234");
    }

    private String json() {
        String json = "{\n" +
                "  \"id\": \"1234\",\n" +
                "  \"createdBy\": \"Alice\",\n" +
                "  \"lastModifiedBy\": \"Bob\",\n" +
                "  \"version\": 1,\n" +
                "  \"submitter\": {\"email\": \"alice@thing.ac.uk\"},\n" +
                "  \"team\": {\"name\": \"subs.testTeam\"},\n" +
                "  \"submissionId\": \"s456\",\n" +
                "  \"uniqueName\": \"my-sample\",\n" +
                "  \"documentType\": \"samples\",\n" +
                "  \"status\": \"Draft\",\n" +
                "  \"document\": [\n" +
                "    {\"a\": \"b\"},\n" +
                "    \"could be anything\"\n" +
                "  ],\n" +
                "  \"refs\": [\n" +
                "    {\"refType\": \"samples\", \"accession\": \"a1\", \"uniqueName\": \"un1\", \"teamName\": \"self.testTeam\", \"sourceJsonPath\": \"$.['ref']\"}\n" +
                "  ],\n" +
                "  \"uploadedFileRefs\": [\n" +
                "    {\"uploadedFile\": \"test.txt\", \"sourceJsonPath\": \"$.['file']\"}\n" +
                "  ]\n" +
                "}";
        return json;
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




