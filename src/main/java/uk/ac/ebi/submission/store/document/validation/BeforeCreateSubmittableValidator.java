package uk.ac.ebi.submission.store.document.validation;

import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.document.DocumentMongoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateSubmittableValidator")
@RequiredArgsConstructor
@Slf4j
public class BeforeCreateSubmittableValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Document.class.equals(clazz);
    }

    @NonNull
    private DocumentMongoRepository documentMongoRepository;

    @NonNull
    private CommonSubmittableValidation commonSubmittableValidation;

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        Document document = (Document) target;

        log.debug("Before create validation of document {}", document);
        log.info("Before create validation of document {}", document.getId());

        commonSubmittableValidation.validate(document, errors);

        //TODO if alias exists for refType + team, ensure consistent with existing record (refType, alias)
        //TODO check that there's at mose only one incomplete version of the record being edited
    }
}
