package uk.ac.ebi.submission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.ac.ebi.submission.mapping.ObjectMappingConfig;
import uk.ac.ebi.submission.messaging.MessageConverterConfig;

@SpringBootApplication(scanBasePackageClasses = {
        SubmissionsApplication.class,
        ObjectMappingConfig.class,
        MessageConverterConfig.class
})
public class SubmissionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubmissionsApplication.class, args);
    }

}
