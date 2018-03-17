package uk.ac.ebi.submission.store.submission.validation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.errors.ApiError;
import uk.ac.ebi.submission.store.errors.SubsApiErrors;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionStatus;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;

import java.util.Map;
import java.util.Optional;

@Component("beforeSaveSubmissionvalidator")
@RequiredArgsConstructor
@Slf4j
public class BeforeSaveSubmissionValidator implements Validator {

    @NonNull private SubmissionMongoRepository repository;

    @NonNull private Map<SubmissionStatus, StatusDescription> submissionStatusDescriptionMap;

    @Override
    public boolean supports(Class<?> clazz) {
        return Submission.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Submission submission = (Submission)target;

        Optional<Submission> optionalOriginalSubmission = repository.findById(submission.getId());

        if (!optionalOriginalSubmission.isPresent()){
            throw new IllegalStateException("Cannot find submission by id during update validation. ID is "+submission.getId());
        }

        Submission original = optionalOriginalSubmission.get();

        //don't change team or submitter


        //if the status has changed, check it s permitted by user, or user is an admin
        if (submission.getStatus() != original.getStatus()){
            //status change attempted, is it acceptable?

            //user must be an admin OR the change must be in the approved transitions list
            boolean userIsAdmin = false; //TODO implement later

            boolean changeIsAcceptable = isAcceptableStatusChange(submission.getStatus(),original.getStatus());

            if (!userIsAdmin && !changeIsAcceptable){
                SubsApiErrors.invalid_status_change.addError(errors,"status");
            }
        }




    }

    private boolean isAcceptableStatusChange(SubmissionStatus newSubmissionStatus, SubmissionStatus oldSubmissionStatus){
        return submissionStatusDescriptionMap.get(oldSubmissionStatus)
                .getUserTransitions()
                .contains(newSubmissionStatus);
    }


}
