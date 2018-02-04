package dmr.submissionstore.submittable.services;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import dmr.submissionstore.submittable.Ref;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Given a string representing a JSON document,
 * return a collection of any Refs discovered
 *
 * refs match these patterns:
 *
 * {"ref": {refBody}, "type": "a type"}
 * or
 * {"services": [{refBody},...], "type": "a type"}
 *
 * refbody should either:
 *   accession
 *   or
 *   teamName + uniqueName
 *   or
 *   uniqueName (we'll assume it's the same team as the owner of the document)
 *
 *
 *  You can include any other information, but it won't be extracted into the ref(it's still in the document)
 */
@Component
@Slf4j
public class RefExtractor {
    
    private static final JsonPath singleRefFinder = JsonPath.compile("$..[?(@.ref && @.type)]");
    private static final JsonPath multiRefFinder = JsonPath.compile("$..[?(@.refs && @.type)]");

    private static final Configuration pathListConfiguration = pathListProviderConfiguration();
    private static final Configuration valueProviderConfiguration = valueProviderConfiguration();

    private Collection<Ref> extractRefs(ReadContext pathReadDocument, ReadContext valueReadContext) {
        log.info("Extracting refs from document");
        log.debug("Extracting refs from document {}", pathReadDocument);

        final Set<Ref> refs = new HashSet<>();

        refs.addAll(extractSingleRefs(pathReadDocument, valueReadContext));
        refs.addAll(extractMultiRefs(pathReadDocument, valueReadContext));


        log.debug("Extracted refs from document {} {}", pathReadDocument, refs);

        return refs;
    }

    private Collection<Ref> extractSingleRefs(ReadContext pathReadDocument, ReadContext valueReadContext) {
        log.debug("Extracting single refs from document {}", pathReadDocument);

        final Set<Ref> refs = new HashSet<>();

        List<String> paths = pathReadDocument.read(singleRefFinder);

        for (String path : paths) {
            SingleRef singleRef = valueReadContext.read(path, SingleRef.class);
            if (singleRef.ref != null) {
                refs.add(singleRef.asRef(path));
            }
        }

        return refs;
    }

    private Collection<Ref> extractMultiRefs(ReadContext pathReadDocument, ReadContext valueReadContext) {
        log.debug("Extracting multi refs from document {}", pathReadDocument);

        final Set<Ref> refs = new HashSet<>();

        List<String> paths = pathReadDocument.read(multiRefFinder);

        for (String path : paths) {
            MultiRef multiRef = valueReadContext.read(path, MultiRef.class);
            if (multiRef.refs != null) {
                refs.addAll(multiRef.asRefs(path));
            }
        }

        return refs;
    }


    public Collection<Ref> extractRefs(String document) {
        log.debug("Converting string to json {}", document);
        log.info("Converting string to json");

        ReadContext pathReadContext = JsonPath.using(pathListConfiguration).parse(document);
        ReadContext valueReadContext = JsonPath.using(valueProviderConfiguration).parse(document);

        log.info("Converted string to json");
        return this.extractRefs(pathReadContext, valueReadContext);

    }

    private static Configuration pathListProviderConfiguration() {
        return Configuration
                .builder()
                .options(Option.AS_PATH_LIST, Option.ALWAYS_RETURN_LIST)
                .build();
    }

    private static Configuration valueProviderConfiguration() {
        return Configuration
                .builder()
                .build();
    }


    @Data
    private static class RefBody {
        private String uniqueName;
        private String accession;
        private String teamName;

        Ref.RefBuilder asRefBuilder() {
            return Ref.builder()
                    .accession(accession)
                    .uniqueName(uniqueName)
                    .teamName(teamName)
                    ;
        }
    }

    @Data
    private static class SingleRef {
        private String type;
        private RefBody ref;

        Ref asRef(String jsonPath) {
            return ref.asRefBuilder()
                    .type(type)
                    .sourceJsonPath(jsonPath + "['ref']")
                    .build();
        }
    }

    @Data
    private static class MultiRef {
        private String type;
        private RefBody[] refs;

        Collection<Ref> asRefs(String jsonPath) {
            Set<Ref> refsBuilt = new HashSet<>();

            for (int i = 0; i < refs.length; i++) {
                if (refs[i] != null) {
                    String elementPath = jsonPath + "['refs'][" + i + "]";

                    refsBuilt.add(
                            refs[i].asRefBuilder()
                                    .type(type)
                                    .sourceJsonPath(elementPath)
                                    .build()
                    );
                }
            }
            return refsBuilt;
        }
    }


}
