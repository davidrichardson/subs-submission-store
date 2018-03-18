package uk.ac.ebi.submission.store.submissionDocument.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SubmissionDocumentController {

    @NonNull
    private SubmissionDocumentMongoRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "/submissions/{submissionId}/documentStatusSummary")
    public ResponseEntity<Resource<Map<String, Map<String, Integer>>>> summariseDocumentStatus(@PathVariable("submissionId")  String submissionId){

        Map<String, Map<String, Integer>> summary = repository.summariseProcessingStatusByType(submissionId);

        Resource<Map<String, Map<String, Integer>>> resource = new Resource<>(summary);

        return ResponseEntity.ok(resource);
    }


}
