package uk.ac.ebi.submission.store.submittable;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.UriTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittableType.SubmittableType;
import uk.ac.ebi.submission.store.validationResult.ValidationResult;
import uk.ac.ebi.submission.store.validationResult.ValidationResultRelNames;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmittableResourceProcessor.class)
public class SubmittableResourceProcessorTest {

    @Autowired
    private SubmittableResourceProcessor submittableResourceProcessor;

    @MockBean
    private RepositoryEntityLinks repositoryEntityLinks;

    private Submittable submittable;
    private Resource<Submittable> resource;

    private Link submissionLink;
    private Link typeLink;
    private Link validationResultLink;

    private Link validationResultTemplatedLink;

    @Before
    public void buildUp() {
        submittable = new Submittable();
        submittable.setId("DOC_ID");
        submittable.setSubmittableTypeId("TYPE_ID");
        submittable.setSubmissionId("SUB_ID");

        resource = new Resource<>(submittable);

        submissionLink = new Link("/submissions/SUB_ID", "submission");
        typeLink = new Link("/submittableTypes/TYPE_ID", "submittableType");

        validationResultTemplatedLink = new Link(new UriTemplate("/validationResults/search/findOneBySubmittableId{?submittableId}"), "validationResult");
        validationResultLink = new Link("/validationResults/search/findOneBySubmittableId?submittableId=DOC_ID", "validationResult");
    }


    @Test
    public void testLinks() {

        when(repositoryEntityLinks.linkToSingleResource(Submission.class, "SUB_ID")).thenReturn(submissionLink);
        when(repositoryEntityLinks.linkToSingleResource(SubmittableType.class, "TYPE_ID")).thenReturn(typeLink);
        when(repositoryEntityLinks.linkToSearchResource(ValidationResult.class, ValidationResultRelNames.BY_SUBMITTABLE_ID)).thenReturn(validationResultTemplatedLink);


        submittableResourceProcessor.process(resource);

        List<Link> actualLinks = resource.getLinks();
        List<Link> expectedLinks = Arrays.asList(
                submissionLink,
                typeLink,
                validationResultLink
        );

        assertThat(actualLinks, equalTo(expectedLinks));
    }
}
