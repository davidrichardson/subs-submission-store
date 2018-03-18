package uk.ac.ebi.submission.store.submissionDocument.rest;

import java.util.Map;

public interface SubmissionDocumentRepositoryCustom {

    Map<String, Map<String, Integer>> summariseProcessingStatusByType(String submissionId);
}
