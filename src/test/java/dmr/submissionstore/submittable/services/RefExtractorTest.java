package dmr.submissionstore.submittable.services;

import dmr.submissionstore.submittable.Ref;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RefExtractorTest {

    private String jsonDoc;
    private Set<Ref> expected;

    private RefExtractor refExtractor;

    @Before
    public void buildUp() {
        refExtractor = new RefExtractor();

        jsonDoc = "{\n" +
                "  \"uniqueName\": \"anAssay\",\n" +
                "  \"study\": {\n" +
                "    \"ref\": {\n" +
                "      \"uniqueName\": \"aStudy\"\n" +
                "    },\n" +
                "    \"type\": \"study\"\n" +
                "  },\n" +
                "  \"thing\": {\n" +
                "    \"improbable\": {\n" +
                "      \"type\": \"improbable\",\n" +
                "      \"ref\": {\n" +
                "        \"uniqueName\": \"whoknows\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"otherThing\": {\n" +
                "    \"unlikelyList\": {\n" +
                "      \"type\": \"unlikely\",\n" +
                "      \"refs\": [\n" +
                "        {\n" +
                "          \"uniqueName\": \"that\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"uniqueName\": \"theOther\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"samples\": {\n" +
                "    \"type\": \"sample\",\n" +
                "    \"refs\": [\n" +
                "      {\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        expected = new HashSet<>();

        Stream.of(
                Ref.builder().type("study").uniqueName("aStudy").sourceJsonPath("$['study']['ref']").build(),
                Ref.builder().type("improbable").uniqueName("whoknows").sourceJsonPath("$['thing']['improbable']['ref']").build(),
                Ref.builder().type("unlikely").uniqueName("that").sourceJsonPath("$['otherThing']['unlikelyList']['refs'][0]").build(),
                Ref.builder().type("unlikely").uniqueName("theOther").sourceJsonPath("$['otherThing']['unlikelyList']['refs'][1]").build(),
                Ref.builder().type("sample").sourceJsonPath("$['samples']['refs'][0]").build()
        ).forEach(
                ref -> expected.add(ref)
        );

    }

    @Test
    public void extractRefs() {
        Collection<Ref> actual = refExtractor.extractRefs(jsonDoc);

        assertThat(actual, notNullValue());
        assertThat(actual,containsInAnyOrder(expected.stream().map(IsEqual::equalTo).collect(Collectors.toList())));
    }

}
