package uk.ac.ebi.submission.store.document.validation;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.errors.SubsApiErrors;
import uk.ac.ebi.submission.store.submission.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.document.DocumentOperationControl;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.documentType.DocumentTypeMongoRepository;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;
import uk.ac.ebi.submission.store.document.DocumentMongoRepository;
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
    private DocumentMongoRepository documentMongoRepository;

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    @NonNull
    private DocumentOperationControl documentOperationControl;

    @NonNull
    private DocumentTypeMongoRepository documentTypeMongoRepository;

    public void validate(Document document, Errors errors) {
        log.debug("common validation of document {}", document);
        log.info("common validation of document {}", document.getId());


        minimalDocumentCheck(document, errors);

        uniquenessCheck(document, errors);

        documentTypeChecks(document, errors);

        submissionStatusCheck(document, errors);


        log.debug("common validation of document complete {}", errors);
    }

     void documentTypeChecks(Document document, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "documentType");

        if (document.getDocumentType() != null){
            Optional<DocumentType> submittableTypeOptional = documentTypeMongoRepository.findByTypeName(
                    document.getDocumentType()
            );

            if (!submittableTypeOptional.isPresent()){
                SubsApiErrors.invalid.addError(errors,"documentType");
            }
        }
    }

    protected void submissionStatusCheck(Document document, Errors errors) {
        if (document.getSubmissionId() != null){
            Optional<Submission> optionalSubmission = submissionMongoRepository.findById( document.getSubmissionId() );

            if (optionalSubmission.isPresent() && !submissionOperationControl.isChangeable(optionalSubmission.get())){
                SubsApiErrors.resource_locked.addError(errors,"submission");

            }

            if (!optionalSubmission.isPresent()){
                SubsApiErrors.invalid.addError(errors,"submissionId");
            }
        }
    }

    protected void submittableStatusCheck(Document document, Errors errors){
        if (document.getStatus() != null){

            if (!documentOperationControl.isChangeable(document)){
                SubsApiErrors.resource_locked.addError(errors);
            }

        }
    }

    private void uniquenessCheck(Document document, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "uniqueName");
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "submissionId");
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "documentType");


        if (document.getUniqueName() != null
                && document.getSubmissionId() != null
                && document.getDocumentType() != null) {

            Document existingDocument = documentMongoRepository.findOneBySubmissionIdAndUniqueNameAndDocumentType(document);

            if (!existingDocument.getId().equals(document.getId())) {
                SubsApiErrors.already_exists.addError(errors);
            }
        }
    }

    private void minimalDocumentCheck(Document document, Errors errors) {
        SubsApiErrors.rejectIfEmptyOrWhitespace(errors, "document");

        if (document.getContent() != null){
            try {
                JsonPath.parse(document.getContent());
            }
            catch (InvalidJsonException e){
                SubsApiErrors.invalid.addError(errors,"document");
            }
        }
    }
}
