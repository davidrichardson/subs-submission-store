package uk.ac.ebi.submission.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {uk.ac.ebi.submission.mapping.ObjectMappingConfig.class, uk.ac.ebi.submission.messaging.MessageConverterConfig.class})
public class SubsValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubsValidationApplication.class, args);
    }
}
