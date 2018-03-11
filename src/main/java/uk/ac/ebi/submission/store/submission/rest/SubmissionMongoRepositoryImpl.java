package uk.ac.ebi.submission.store.submission.rest;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.submission.Submission;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class SubmissionMongoRepositoryImpl implements SubmissionRepositoryCustom {

    @NonNull
    private MongoTemplate mongoTemplate;

    public Page<Submission> findAll(Pageable pageable) {
        return new PageImpl<Submission>(Collections.emptyList());
    }

    @Override
    public Map<String, Integer> statusCountsByTeam(List<String> teamNames) {
        Aggregation agg = Aggregation.newAggregation(
                match(where("team.name").in(teamNames)),
                group("status").count().as("count"),
                project("count").and("_id").as("status")
        );

        AggregationResults<StatusSummary> aggregationResults = mongoTemplate.aggregate(
                agg, Submission.class, StatusSummary.class
        );

        List<StatusSummary> statusSummaries = aggregationResults.getMappedResults();

        Map<String, Integer> statusCounts = new TreeMap<>();

        for (StatusSummary statusSummary : statusSummaries) {
            statusCounts.put(statusSummary.getStatus(), statusSummary.getCount());
        }
        return statusCounts;
    }

    @Data
    private class StatusSummary {
        private String status;
        private int count;
    }


}
