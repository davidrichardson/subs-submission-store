package uk.ac.ebi.submission.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import uk.ac.ebi.submission.mapping.ObjectMappingConfig;
import uk.ac.ebi.submission.messaging.MessageConverterConfig;
import uk.ac.ebi.submission.store.root.RepositoryLinksResourceProcessor;

@SpringBootApplication(scanBasePackageClasses = {
        SubsSubmissionStoreApplication.class,
        ObjectMappingConfig.class,
        MessageConverterConfig.class
})
public class SubsSubmissionStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubsSubmissionStoreApplication.class, args);
    }

}
