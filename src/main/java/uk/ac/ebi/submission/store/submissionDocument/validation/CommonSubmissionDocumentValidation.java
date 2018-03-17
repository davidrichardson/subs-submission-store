package uk.ac.ebi.submission.store.submissionDocument.validation;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.errors.SubsApiErrors;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocumentOperationControl;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.documentType.DocumentTypeMongoRepository;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;
import uk.ac.ebi.submission.store.submissionDocument.rest.SubmissionDocumentMongoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonSubmissionDocumentValidation {

    @NonNull
    private SubmissionDocumentMongoRepository submissionDocumentMongoRepository;

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    @NonNull
    private SubmissionDocumentOperationControl submissionDocumentOperationControl;

    @NonNull
    private DocumentTypeMongoRepository documentTypeMongoRepository;

    public void validate(SubmissionDocument submissionDocument, Errors errors) {
        log.debug("common validation of submissionDocument {}", submissionDocument);



        minimalDocumentCheck(submissionDocument, errors);

        uniquenessCheck(submissionDocument, errors);

        documentTypeChecks(submissionDocument, errors);

        submissionStatusCheck(submissionDocument, errors);


        log.debug("common validation of submissionDocument complete {}", errors);
    }

     void documentTypeChecks(SubmissionDocument submissionDocument, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "documentType");

        if (submissionDocument.getDocumentType() != null){
            Optional<DocumentType> submittableTypeOptional = documentTypeMongoRepository.findOneByTypeName(
                    submissionDocument.getDocumentType()
            );

            if (!submittableTypeOptional.isPresent()){
                SubsApiErrors.invalid.addError(errors,"documentType");
            }
        }
    }

    protected void submissionStatusCheck(SubmissionDocument submissionDocument, Errors errors) {
        if (submissionDocument.getSubmissionId() != null){
            Optional<Submission> optionalSubmission = submissionMongoRepository.findById( submissionDocument.getSubmissionId() );

            if (optionalSubmission.isPresent() && !submissionOperationControl.isChangeable(optionalSubmission.get())){
                SubsApiErrors.resource_locked.addError(errors,"submission");

            }

            if (!optionalSubmission.isPresent()){
                SubsApiErrors.invalid.addError(errors,"submissionId");
            }
        }
    }

    protected void submittableStatusCheck(SubmissionDocument submissionDocument, Errors errors){
        if (submissionDocument.getStatus() != null){

            if (!submissionDocumentOperationControl.isChangeable(submissionDocument)){
                SubsApiErrors.resource_locked.addError(errors);
            }

        }
    }

    private void uniquenessCheck(SubmissionDocument submissionDocument, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "uniqueName");
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "submissionId");
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "documentType");


        if (submissionDocument.getUniqueName() != null
                && submissionDocument.getSubmissionId() != null
                && submissionDocument.getDocumentType() != null) {

            Optional<SubmissionDocument> existingSubmissionDocument = submissionDocumentMongoRepository.findOneBySubmissionIdAndUniqueNameAndDocumentType(submissionDocument);

            if (existingSubmissionDocument.isPresent() && !existingSubmissionDocument.get().getId().equals(submissionDocument.getId())) {
                SubsApiErrors.already_exists.addError(errors);
            }
        }
    }

    private void minimalDocumentCheck(SubmissionDocument submissionDocument, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "submissionDocument");

        if (submissionDocument.getContent() != null){
            try {
                JsonPath.parse(submissionDocument.getContent());
            }
            catch (InvalidJsonException e){
                SubsApiErrors.invalid.addError(errors,"submissionDocument");
            }
        }
    }
}
