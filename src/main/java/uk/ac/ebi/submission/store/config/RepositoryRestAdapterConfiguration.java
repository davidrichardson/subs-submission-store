package uk.ac.ebi.submission.store.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import uk.ac.ebi.submission.mapping.ObjectMappingConfig;

@Configuration
class RepositoryRestAdapterConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        ObjectMappingConfig.customizeObjectMapper(objectMapper);
    }

}
