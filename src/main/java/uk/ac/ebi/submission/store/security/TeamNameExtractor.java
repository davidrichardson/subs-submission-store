package uk.ac.ebi.submission.store.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.document.Document;
import uk.ac.ebi.submission.store.document.DocumentMongoRepository;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeamNameExtractor {

    @NonNull
    private DocumentMongoRepository documentMongoRepository;

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;


    public String documentIdTeam(String documentId) {
        Optional<Document> document = documentMongoRepository.findById(documentId);

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
