package uk.ac.ebi.submission.store.submissionDocument.rest;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
@Component
public class SubmissionDocumentMongoRepositoryImpl implements SubmissionDocumentRepositoryCustom{

    @NonNull
    private MongoTemplate mongoTemplate;

    public Page<SubmissionDocument> findAll(Pageable pageable) {
        return new PageImpl<SubmissionDocument>(Collections.emptyList());
    }


    @Override
    public Map<String, Map<String, Integer>> summariseProcessingStatusByType(String submissionId) {
        Aggregation agg = Aggregation.newAggregation(
                submissionMatchOperation(submissionId),
                groupByTypeAndStatus(),
                projectTypeStatusCount()
        );

        AggregationResults<TypeStatusSummary> aggregationResults = mongoTemplate.aggregate(
                agg, SubmissionDocument.class, TypeStatusSummary.class
        );

        List<TypeStatusSummary> statusSummaries = aggregationResults.getMappedResults();

        Map<String, Map<String, Integer>> typeStatusCounts = new HashMap<>();

        for (TypeStatusSummary typeStatusSummary : statusSummaries) {

            if (!typeStatusCounts.containsKey(typeStatusSummary.getType())) {
                typeStatusCounts.put(typeStatusSummary.getType(), new HashMap<>());
            }

            typeStatusCounts
                    .get(typeStatusSummary.getType())
                    .put(typeStatusSummary.getStatus(), typeStatusSummary.getCount());

        }


        return typeStatusCounts;
    }

    private MatchOperation submissionMatchOperation(String submissionId) {
        return match(where("submissionId").is(submissionId));
    }

    private GroupOperation groupByTypeAndStatus() {
        return group("documentType", "status").count().as("count");
    }

    private ProjectionOperation projectTypeStatusCount() {
        return project("count").and("_id.status").as("status").and("_id.documentType").as("type");
    }

    @Data
    private static class TypeStatusSummary {
        private String status;
        private String type;
        private int count;
    }
}
