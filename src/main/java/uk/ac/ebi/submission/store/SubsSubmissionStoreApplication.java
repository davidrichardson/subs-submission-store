package uk.ac.ebi.submission.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.ac.ebi.submission.mapping.ObjectMappingConfig;
import uk.ac.ebi.submission.messaging.MessageConverterConfig;

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
