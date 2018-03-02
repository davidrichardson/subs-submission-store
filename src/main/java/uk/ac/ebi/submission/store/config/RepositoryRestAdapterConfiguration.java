package uk.ac.ebi.submission.store.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import uk.ac.ebi.submission.mapping.ObjectMappingConfig;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittable.Submittable;

@Configuration
class RepositoryRestAdapterConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        ObjectMappingConfig.customizeObjectMapper(objectMapper);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(
                Submission.class,
                Submittable.class
        );
    }
}
