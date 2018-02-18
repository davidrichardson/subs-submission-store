package uk.ac.ebi.submission.store.submittable.extractors;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;


public class ExtractorJsonPathConfig {


    public static Configuration pathListProviderConfiguration() {
        return Configuration
                .builder()
                .options(Option.AS_PATH_LIST, Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS)
                .build();
    }

    public static Configuration valueProviderConfiguration() {
        return Configuration
                .builder()
                .jsonProvider(new JacksonJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();
    }



}
