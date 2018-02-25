package uk.ac.ebi.submission.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {uk.ac.ebi.submission.mapping.ObjectMappingConfig.class,uk.ac.ebi.submission.messaging.MessageConverterConfig.class})
public class SubsSubmissionStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubsSubmissionStoreApplication.class, args);
	}
}
