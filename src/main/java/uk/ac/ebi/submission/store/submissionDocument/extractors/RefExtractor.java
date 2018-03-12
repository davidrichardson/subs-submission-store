package uk.ac.ebi.submission.store.submissionDocument.extractors;

import com.jayway.jsonpath.*;
import uk.ac.ebi.submission.store.submissionDocument.Ref;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Given a string representing a JSON submissionDocument,
 * return a collection of any Refs discovered
 * <p>
 * refs match these patterns:
 * <p>
 * {"ref": {refBody}, "refType": "a refType"}
 * or
 * {"extractors": [{refBody},...], "refType": "a refType"}
 * <p>
 * refbody should either:
 * accession
 * or
 * teamName + uniqueName
 * or
 * uniqueName (we'll assume it's the same team as the owner of the submissionDocument)
 * <p>
 * <p>
 * You can include any other information, but it won't be extracted into the ref(it's still in the submissionDocument)
 */
@Component
@Slf4j
public class RefExtractor {

    private static final JsonPath singleRefFinder = JsonPath.compile("$..[?(@.refType)]");

    private static final Configuration pathListConfiguration = ExtractorJsonPathConfig.pathListProviderConfiguration();
    private static final Configuration valueProviderConfiguration = ExtractorJsonPathConfig.valueProviderConfiguration();

    private Collection<Ref> extractRefs(ReadContext pathReadDocument, ReadContext valueReadContext) {
        log.info("Extracting refs from submissionDocument");
        log.debug("Extracting refs from submissionDocument {}", pathReadDocument);

        final Set<Ref> refs = new HashSet<>();

        List<String> paths = pathReadDocument.read(singleRefFinder);

        for (String path : paths) {
            Ref ref = valueReadContext.read(path, Ref.class);
            if (ref != null) {
                ref.setSourceJsonPath(path);
                refs.add(ref);
            }
        }

        log.debug("Extracted refs from submissionDocument {} {}", pathReadDocument, refs);

        return refs;
    }

    public Collection<Ref> extractRefs(String document) {
        if (document == null) {
            log.debug("null submissionDocument");
            return Collections.emptyList();
        }

        log.debug("Converting string to json {}", document);
        log.info("Converting string to json");

        try {
            ReadContext pathReadContext = JsonPath.using(pathListConfiguration).parse(document);
            ReadContext valueReadContext = JsonPath.using(valueProviderConfiguration).parse(document);

            log.info("Converted string to json");
            return this.extractRefs(pathReadContext, valueReadContext);
        } catch (InvalidJsonException e) {
            log.debug("invalid json submissionDocument");
            return Collections.emptyList();
        }

    }
}
