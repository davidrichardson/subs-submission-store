package dmr.submissionstore.submittable.extractors;

import dmr.submissionstore.submittable.UploadedFileRef;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FileRefExtractorTest {

    private String jsonDoc;
    private Set<UploadedFileRef> expected;

    private FileRefExtractor refExtractor = new FileRefExtractor();

    @Test
    public void extractRefs() {
        String jsonDoc = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\" : \"doe\",\n" +
                "  \"age\"      : 26,\n" +
                "  \"address\"  : {\n" +
                "    \"streetAddress\": \"naist street\",\n" +
                "    \"city\"         : \"Nara\",\n" +
                "    \"postalCode\"   : \"630-0192\",\n" +
                "    \"uploadedFile\": \"alice.txt\"\n" +
                "  },\n" +
                "  \"phoneNumbers\": [\n" +
                "    {\n" +
                "      \"refType\"  : \"iPhone\",\n" +
                "      \"number\": \"0123-4567-8888\",\n" +
                "      \"uploadedFile\": \"bob.txt\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"refType\"  : \"home\",\n" +
                "      \"number\": \"0123-4567-8910\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Set<UploadedFileRef> expected = new HashSet<>();

        expected.addAll(Arrays.asList(
                uploadedFileRef("alice.txt", "$['address']"),
                uploadedFileRef("bob.txt", "$['phoneNumbers'][0]")

        ));

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual, notNullValue());
        assertThat(actual, containsInAnyOrder(expected.stream().map(IsEqual::equalTo).collect(Collectors.toList())));

    }

    private UploadedFileRef uploadedFileRef(String file, String sourceJsonPath) {
        UploadedFileRef u = new UploadedFileRef();
        u.setUploadedFile(file);
        u.setSourceJsonPath(sourceJsonPath);
        return u;
    }

    @Test
    public void handleInvalidDocument() {
        String jsonDoc = "{";
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual, empty());
    }

    @Test
    public void handleNullDocument() {
        String jsonDoc = null;
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual, empty());

    }

    @Test
    public void handleEmptyDocument() {
        String jsonDoc = "{}";
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual, empty());

    }
}
