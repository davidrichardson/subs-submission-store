package uk.ac.ebi.submission.store.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionSubmissionDocumentMongoRepository;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeamNameExtractor {

    @NonNull
    private SubmissionSubmissionDocumentMongoRepository submissionDocumentMongoRepository;

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;


    public String documentIdTeam(String documentId) {
        Optional<SubmissionDocument> document = submissionDocumentMongoRepository.findById(documentId);

        if (!document.isPresent()) {
            throw new ResourceNotFoundException();
        }

        return document.get().getTeam().getName();
    }

    public String submissionIdTeam(String submissionId) {
        Optional<Submission> submission = submissionMongoRepository.findById(submissionId);

        if (!submission.isPresent()) {
            throw new ResourceNotFoundException();
        }

        return submission.get().getTeam().getName();
    }
}
