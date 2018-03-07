package uk.ac.ebi.submission.store.document.extractors;

import uk.ac.ebi.submission.store.document.Ref;
import uk.ac.ebi.submission.store.document.UploadedFileRef;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RefExtractorTest {

    private RefExtractor refExtractor = new RefExtractor();

    @Test
    public void extractRefs() {
        String jsonDoc = "{\n" +
                "  \"uniqueName\": \"anAssay\",\n" +
                "  \"study\": {\n" +
                "    \"refType\": \"study\",\n" +
                "    \"uniqueName\": \"aStudy\"\n" +
                "  },\n" +
                "  \"thing\": {\n" +
                "    \"improbable\": {\n" +
                "      \"refType\": \"improbable\",\n" +
                "      \"uniqueName\": \"whoknows\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"otherThing\": {\n" +
                "    \"unlikelyList\": {\n" +
                "      \"refs\": [\n" +
                "        {\n" +
                "          \"refType\": \"unlikely\",\n" +
                "          \"uniqueName\": \"that\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"refType\": \"unlikely\",\n" +
                "          \"uniqueName\": \"theOther\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"samples\": {\n" +
                "    \"refs\": [\n" +
                "      {\n" +
                "        \"refType\": \"sample\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        Set<Ref> expected = new HashSet<>();
        expected.addAll(
                Arrays.asList(
                        ref("study", "aStudy", "$['study']"),
                        ref("improbable", "whoknows", "$['thing']['improbable']"),
                        ref("unlikely", "that", "$['otherThing']['unlikelyList']['refs'][0]"),
                        ref("unlikely", "theOther", "$['otherThing']['unlikelyList']['refs'][1]"),
                        ref("sample", null, "$['samples']['refs'][0]")
                ));

        Collection<Ref> actual = refExtractor.extractRefs(jsonDoc);

        assertThat(actual, notNullValue());
        assertThat(actual, containsInAnyOrder(expected.stream().map(IsEqual::equalTo).collect(Collectors.toList())));
    }

    @Test
    public void extractNoRefsFromEmptyObject() {
        String jsonDoc = "{}";

        Collection<Ref> actual = refExtractor.extractRefs(jsonDoc);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(0));
    }

    private Ref ref(String type, String uniqueName, String jsonSourcePath) {
        Ref r = new Ref();
        r.setRefType(type);
        r.setUniqueName(uniqueName);
        r.setSourceJsonPath(jsonSourcePath);
        return r;
    }


    @Test
    public void handleInvalidDocument() {
        String jsonDoc = "{";
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<Ref> actual = refExtractor.extractRefs(jsonDoc);

        assertThat(actual, empty());
    }

    @Test
    public void handleNullDocument() {
        String jsonDoc = null;
        Set<UploadedFileRef> expected = new HashSet<>();

        Collection<Ref> actual = refExtractor.extractRefs(jsonDoc);

        assertThat(actual, empty());

    }
}
