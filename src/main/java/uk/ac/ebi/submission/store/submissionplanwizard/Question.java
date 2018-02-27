package uk.ac.ebi.submission.store.submissionplanwizard;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Question {

    private String questionText;

    private Map<String,Question> furtherQuestions = new HashMap<>();

    private Map<String,SubmissionPlan> submissionPlans = new HashMap<>();


}
