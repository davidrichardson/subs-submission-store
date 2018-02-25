package uk.ac.ebi.submission.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {uk.ac.ebi.submission.mapping.ObjectMappingConfig.class,uk.ac.ebi.submission.messaging.MessageConverterConfig.class})
public class SubsDispatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubsDispatchApplication.class, args);
	}
}
