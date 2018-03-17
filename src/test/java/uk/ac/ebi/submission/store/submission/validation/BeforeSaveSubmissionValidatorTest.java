package uk.ac.ebi.submission.store.submission.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.common.model.Team;
import uk.ac.ebi.submission.store.config.StatusConfiguration;
import uk.ac.ebi.submission.store.errors.SubsApiErrors;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionStatus;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;

import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BeforeSaveSubmissionValidator.class, StatusConfiguration.class})
public class BeforeSaveSubmissionValidatorTest {

    @Autowired
    private BeforeSaveSubmissionValidator validator;

    @Autowired
    private Map<SubmissionStatus, StatusDescription> submissionStatusDescriptionMap;

    @MockBean
    private SubmissionMongoRepository repository;

    @MockBean
    private Errors errors;

    Submission submission;

    Submission originalSubmission;


    @Before
    public void before() {
        submission = submission();
        originalSubmission = submission();

        Mockito.when(repository.findById(originalSubmission.getId())).thenReturn(Optional.of(originalSubmission));
    }

    private Submission submission() {
        Submission s = new Submission();
        s.setId("1");
        s.setTeam(Team.of("subs.team"));
        s.setStatus(SubmissionStatus.Draft);
        return s;
    }

    @Test
    public void user_can_turn_draft_to_submitted() {
        submission.setStatus(SubmissionStatus.Submitted);

        validator.validate(submission, errors);

        Mockito.verify(errors, Mockito.never()).rejectValue(Mockito.any(), Mockito.any(), Mockito.any());


    }

    @Test
    public void user_cannot_turn_submitted_to_draft() {
        originalSubmission.setStatus(SubmissionStatus.Submitted);

        validator.validate(submission, errors);

        Mockito.verify(errors).rejectValue(Mockito.eq("status"), Mockito.eq(SubsApiErrors.invalid_status_change.name()), Mockito.any());
    }

}
