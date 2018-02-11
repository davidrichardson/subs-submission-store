package dmr.submissionstore.submittable.validation;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import dmr.submissionstore.errors.SubsApiErrors;
import dmr.submissionstore.submission.Submission;
import dmr.submissionstore.submission.SubmissionMongoRepository;
import dmr.submissionstore.submission.SubmissionOperationControl;
import dmr.submissionstore.submittable.Submittable;
import dmr.submissionstore.submittable.SubmittableMongoRepository;
import dmr.submissionstore.submittableType.SubmittableType;
import dmr.submissionstore.submittableType.SubmittableTypeMongoRepository;
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
    private SubmittableTypeMongoRepository submittableTypeMongoRepository;

    public void validate(Submittable submittable, Errors errors) {
        log.debug("common validation of submittable {}", submittable);
        log.info("common validation of submittable {}", submittable.getId());


        minimalDocumentCheck(submittable, errors);

        uniquenessCheck(submittable, errors);

        documentTypeChecks(submittable, errors);

        submissionCheck(submittable, errors);


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

    protected void submissionCheck(Submittable submittable, Errors errors) {
        if (submittable.getSubmissionId() != null){
            Optional<Submission> optionalSubmission = submissionMongoRepository.findById( submittable.getSubmissionId() );

            if (optionalSubmission.isPresent() && !submissionOperationControl.isChangeable(optionalSubmission.get())){
                SubsApiErrors.resource_locked.addError(errors);

            }

            if (!optionalSubmission.isPresent()){
                SubsApiErrors.invalid.addError(errors,"submissionId");
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
