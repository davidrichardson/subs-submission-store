package dmr.submissionstore.submittable.extractors;

import dmr.submissionstore.submittable.UploadedFileRef;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

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
                "      \"type\"  : \"iPhone\",\n" +
                "      \"number\": \"0123-4567-8888\",\n" +
                "      \"uploadedFile\": \"bob.txt\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\"  : \"home\",\n" +
                "      \"number\": \"0123-4567-8910\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Set<UploadedFileRef> expected = new HashSet<>();

        Stream.of(
                UploadedFileRef.builder().uploadedFile("alice.txt").sourceJsonPath("$['address']['uploadedFile']").build(),
                UploadedFileRef.builder().uploadedFile("bob.txt").sourceJsonPath("$['phoneNumbers'][0]['uploadedFile']").build()

        ).forEach(
                ref -> expected.add(ref)
        );

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual, notNullValue());
        assertThat(actual,containsInAnyOrder(expected.stream().map(IsEqual::equalTo).collect(Collectors.toList())));

    }

    @Test
    public void handleInvalidDocument() {
        String jsonDoc = "{";
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual,empty());
    }

    @Test
    public void handleNullDocument() {
        String jsonDoc = null;
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<UploadedFileRef> actual = refExtractor.extractFileRefs(jsonDoc);

        assertThat(actual,empty());

    }
}
