package uk.ac.ebi.submission.store.submissionDocument.extractors;

import com.jayway.jsonpath.*;
import uk.ac.ebi.submission.store.submissionDocument.UploadedFileRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Given a string representing a JSON submissionDocument,
 * return a collection of any FileRefs discovered
 * <p>
 * file refs have keys matching this pattern
 * <p>
 * {"uploadedFile": "my-file-name.txt"}
 * <p>
 * You can include any other information, but it won't be extracted into the uploaded file ref
 * (it's still in the submissionDocument)
 */
@Component
@Slf4j
public class FileRefExtractor {

    private static final JsonPath finder = JsonPath.compile("$..[?(@.uploadedFile)]");

    private static final Configuration pathListConfiguration = ExtractorJsonPathConfig.pathListProviderConfiguration();
    private static final Configuration valueProviderConfiguration = ExtractorJsonPathConfig.valueProviderConfiguration();

    private Collection<UploadedFileRef> extractFileRefs(ReadContext pathReadContext, ReadContext valueReadContext) {
        log.debug("Extracting single refs from submissionDocument {}", pathReadContext);

        final Set<UploadedFileRef> uploadedFileRefs = new HashSet<>();

        List<String> paths = pathReadContext.read(finder);

        for (String path : paths) {
            UploadedFileRef uploadedFile = valueReadContext.read(path, UploadedFileRef.class);

            if (uploadedFile != null) {
                uploadedFile.setSourceJsonPath(path);
                uploadedFileRefs.add(
                        uploadedFile
                );
            }
        }

        return uploadedFileRefs;
    }

    public Collection<UploadedFileRef> extractFileRefs(String document) {
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
            return this.extractFileRefs(pathReadContext, valueReadContext);
        } catch (InvalidJsonException e) {
            log.debug("invalid json submissionDocument");
            return Collections.emptyList();
        }

    }

}
