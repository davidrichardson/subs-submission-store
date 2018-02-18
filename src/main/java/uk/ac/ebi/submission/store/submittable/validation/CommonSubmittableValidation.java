package uk.ac.ebi.submission.store.submittable.validation;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import uk.ac.ebi.submission.store.errors.SubsApiErrors;
import uk.ac.ebi.submission.store.submission.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.submittable.SubmittableOperationControl;
import uk.ac.ebi.submission.store.submittableType.SubmittableType;
import uk.ac.ebi.submission.store.submittableType.SubmittableTypeMongoRepository;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;
import uk.ac.ebi.submission.store.submittable.Submittable;
import uk.ac.ebi.submission.store.submittable.SubmittableMongoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonSubmittableValidation {

    @NonNull
    private SubmittableMongoRepository submittableMongoRepository;

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    @NonNull
    private SubmittableOperationControl submittableOperationControl;

    @NonNull
    private SubmittableTypeMongoRepository submittableTypeMongoRepository;

    public void validate(Submittable submittable, Errors errors) {
        log.debug("common validation of submittable {}", submittable);
        log.info("common validation of submittable {}", submittable.getId());


        minimalDocumentCheck(submittable, errors);

        uniquenessCheck(submittable, errors);

        documentTypeChecks(submittable, errors);

        submissionStatusCheck(submittable, errors);


        log.debug("common validation of submittable complete {}", errors);
    }

     void documentTypeChecks(Submittable submittable, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "documentType");

        if (submittable.getDocumentType() != null){
            Optional<SubmittableType> submittableTypeOptional = submittableTypeMongoRepository.findByTypeName(
                    submittable.getDocumentType()
            );

            if (!submittableTypeOptional.isPresent()){
                SubsApiErrors.invalid.addError(errors,"documentType");
            }
        }
    }

    protected void submissionStatusCheck(Submittable submittable, Errors errors) {
        if (submittable.getSubmissionId() != null){
            Optional<Submission> optionalSubmission = submissionMongoRepository.findById( submittable.getSubmissionId() );

            if (optionalSubmission.isPresent() && !submissionOperationControl.isChangeable(optionalSubmission.get())){
                SubsApiErrors.resource_locked.addError(errors,"submission");

            }

            if (!optionalSubmission.isPresent()){
                SubsApiErrors.invalid.addError(errors,"submissionId");
            }
        }
    }

    protected void submittableStatusCheck(Submittable submittable, Errors errors){
        if (submittable.getStatus() != null){

            if (!submittableOperationControl.isChangeable(submittable)){
                SubsApiErrors.resource_locked.addError(errors);
            }

        }
    }

    private void uniquenessCheck(Submittable submittable, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "uniqueName");
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "submissionId");
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "documentType");


        if (submittable.getUniqueName() != null
                && submittable.getSubmissionId() != null
                && submittable.getDocumentType() != null) {

            Submittable existingSubmittable = submittableMongoRepository.findOneBySubmissionIdAndUniqueNameAndDocumentType(submittable);

            if (!existingSubmittable.getId().equals(submittable.getId())) {
                SubsApiErrors.already_exists.addError(errors);
            }
        }
    }

    private void minimalDocumentCheck(Submittable submittable, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "document");

        if (submittable.getDocument() != null){
            try {
                JsonPath.parse(submittable.getDocument());
            }
            catch (InvalidJsonException e){
                SubsApiErrors.invalid.addError(errors,"document");
            }
        }
    }
}
