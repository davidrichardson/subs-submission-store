package dmr.submissionstore.submittable.extractors;

import com.jayway.jsonpath.*;
import dmr.submissionstore.submittable.UploadedFileRef;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Given a string representing a JSON document,
 * return a collection of any FileRefs discovered
 * <p>
 * file refs have keys matching this pattern
 * <p>
 * {"uploadedFile": "my-file-name.txt"}
 * <p>
 * You can include any other information, but it won't be extracted into the uploaded file ref
 * (it's still in the document)
 */
@Component
@Slf4j
public class FileRefExtractor {

    private static final JsonPath finder = JsonPath.compile("$..uploadedFile");


    private Collection<UploadedFileRef> extractFileRefs(ReadContext pathReadContext, ReadContext valueReadContext) {
        log.debug("Extracting single refs from document {}", pathReadContext);

        final Set<UploadedFileRef> uploadedFileRefs = new HashSet<>();

        List<String> paths = pathReadContext.read(finder);

        for (String path : paths) {
            String uploadedFile = valueReadContext.read(path, String.class);

            if (uploadedFile != null) {
                uploadedFileRefs.add(
                        UploadedFileRef.builder()
                                .uploadedFile(uploadedFile)
                                .sourceJsonPath(path)
                                .build()
                );
            }
        }

        return uploadedFileRefs;
    }

    public Collection<UploadedFileRef> extractFileRefs(String document) {
        if (document == null){
            log.debug("null document");
            return Collections.emptyList();
        }

        log.debug("Converting string to json {}", document);
        log.info("Converting string to json");

        try {
            ReadContext pathReadContext = JsonPath.using(pathListConfiguration).parse(document);
            ReadContext valueReadContext = JsonPath.using(valueProviderConfiguration).parse(document);

            log.info("Converted string to json");
            return this.extractFileRefs(pathReadContext, valueReadContext);
        }
        catch (InvalidJsonException e){
            log.debug("invalid json document");
            return Collections.emptyList();
        }

    }

    private static final Configuration pathListConfiguration = pathListProviderConfiguration();
    private static final Configuration valueProviderConfiguration = valueProviderConfiguration();


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
    private static class FileRef {
        private String uploadedFile;

        UploadedFileRef asRef(String jsonPath) {
            return UploadedFileRef.builder()
                    .uploadedFile(uploadedFile)
                    .sourceJsonPath(jsonPath)
                    .build();
        }
    }

}
