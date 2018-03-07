package uk.ac.ebi.submission.store.document.serialization;

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
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.document.DocumentStatusEnum;
import uk.ac.ebi.submission.store.document.Ref;
import uk.ac.ebi.submission.store.document.UploadedFileRef;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class DocumentSerializationTest {

    @Autowired
    private JacksonTester<Document> json;

    @Test
    public void testSerialize() throws Exception {
        Document details = submittable();
        assertThat(this.json.write(details)).isEqualToJson("expected.document.json");
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectContent<Document> submittableObjectContent = this.json.read("expected.document.json");
        Document document = submittableObjectContent.getObject();
        assertThat(document)
                .isEqualTo(submittable());
        assertThat(document.getId()).isEqualTo("1234");
    }

    private Document submittable() {
        Document s = new Document();
        s.setId("1234");
        s.setCreatedBy("Alice");
        s.setLastModifiedBy("Bob");
        s.setVersion(1L);
        s.setSubmitter(Submitter.of("alice@thing.ac.uk", null));
        s.setTeam(Team.of("subs.testTeam"));
        s.setSubmissionId("s456");
        s.setUniqueName("my-sample");
        s.setDocumentType("samples");
        s.setStatus(DocumentStatusEnum.Draft);
        s.setContent(JsonHelper.stringToJsonNode("[{\"a\": \"b\"},\"could be anything\"]"));
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




