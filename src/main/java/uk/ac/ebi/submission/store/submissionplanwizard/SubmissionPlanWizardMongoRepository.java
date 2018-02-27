package uk.ac.ebi.submission.store.submissionplanwizard;

import org.springframework.data.mongodb.repository.MongoRepository;


//TODO read by anyone, write by admin
public interface SubmissionPlanWizardMongoRepository extends MongoRepository<SubmissionPlanWizard, String> {



}
