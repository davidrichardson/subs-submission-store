package uk.ac.ebi.submission.store.mockmvc;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.submission.DocumentationProducer;
import uk.ac.ebi.submission.SubmissionsApplication;
import uk.ac.ebi.submission.store.TestUserAndTeamNames;
import uk.ac.ebi.submission.store.common.model.Team;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionStatus;
import uk.ac.ebi.submission.store.submission.rest.SubmissionController;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.submission.rest.SubmissionSearchRelNames;

import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmissionsApplication.class)
@WithMockUser(username = "subs_user", roles = {TestUserAndTeamNames.TEAM_NAME, TestUserAndTeamNames.ADMIN_TEAM_NAME})
@Category(DocumentationProducer.class)
public class SubmissionApiTest {

    private MockMvc mockMvc;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DocumentationHelper.SNIPPET_OUTPUT_DIR);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentationHelper documentationHelper;

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    @Autowired
    private SubmissionMongoRepository submissionMongoRepository;

    @MockBean
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Before
    public void setUp() {
        clearDbs();
        MockMvcRestDocumentationConfigurer docConfig = documentationHelper.docConfig(restDocumentation);
        this.mockMvc = DocumentationHelper.mockMvc(this.context, docConfig);
    }

    @After
    public void tearDown() {
        clearDbs();
    }

    private void clearDbs() {
        submissionMongoRepository.deleteAll();
    }

    @Test
    public void submission_schema() throws Exception {
        this.mockMvc.perform(
                get(documentationHelper.collectionProfile(Submission.class))
                        .accept(DocumentationHelper.JSON_SCHEMA_CONTENT_TYPE)
        ).andExpect(status().isOk())
                .andDo(
                        document("submission-schema",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks()
                                ),
                                responseFields(
                                        fieldWithPath("title").ignored(),
                                        fieldWithPath("description").ignored(),
                                        fieldWithPath("type").ignored(),
                                        fieldWithPath("$schema").ignored(),
                                        subsectionWithPath("properties").ignored(),
                                        subsectionWithPath("definitions").ignored()
                                )
                        )
                );
    }

    @Test
    public void list_submissions_for_a_team() throws Exception {

        //prep data
        addSubmissionToDb("My 1st submission", SubmissionStatus.Completed);
        addSubmissionToDb("My 2nd submission", SubmissionStatus.Draft);


        //prep request url
        Map<String, String> expansionMap = ImmutableMap.<String, String>builder()
                .put("teamName", TestUserAndTeamNames.TEAM_NAME)
                .build();

        Link searchLink = repositoryEntityLinks
                .linkToSearchResource(Submission.class, SubmissionSearchRelNames.TEAM_NAME)
                .expand(expansionMap);

        //do request and verify
        this.mockMvc.perform(
                get(searchLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("submissions-by-team",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionListLinks(),
                                submissionListResponseFields("Submissions known for the team")
                        )
                );
    }

    private LinksSnippet submissionListLinks() {
        return links(
                halLinks(),
                linkWithRel("curies").ignored(),
                linkWithRel("self").ignored()
        );
    }

    @Test
    public void list_submissions_for_a_user() throws Exception {

        //prep data
        addSubmissionToDb("My 1st submission", SubmissionStatus.Completed);
        addSubmissionToDb("My 2nd submission", SubmissionStatus.Draft);


        //prep request url

        Link searchLink = linkTo(
                methodOn(SubmissionController.class)
                        .userSubmissions(null)

        ).withSelfRel();


        //do request and verify
        this.mockMvc.perform(
                get(searchLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("submissions-by-user",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionListLinks(),
                                submissionListResponseFields("List of submissions you can access")
                        )
                );
    }

    private ResponseFieldsSnippet submissionListResponseFields(String description) {
        return responseFields(
                subsectionWithPath("_embedded.subs:submissions[]").description(description),
                DocumentationHelper.paginationBlock(),
                DocumentationHelper.linksResponseField()
        );
    }

    @Test
    public void get_a_single_submission() throws Exception {
        //prep data
        Submission submission = addSubmissionToDb("My 1st submission", SubmissionStatus.Completed);

        //prep request url
        Link searchLink = repositoryEntityLinks
                .linkToSingleResource(submission);

        //do request and verify
        this.mockMvc.perform(
                get(searchLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("get-one-submission",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionLinks(),
                                submissionResponseFields()
                        )
                );
    }

    private LinksSnippet submissionLinks() {
        return links(
                halLinks(),
                linkWithRel("subs:submission").ignored(),
                linkWithRel("curies").ignored(),
                linkWithRel("self").ignored(),
                linkWithRel("subs:submissionDocuments").description("Templated links to documents for this submission"),
                linkWithRel("subs:documentStatusSummary").description("")
        );
    }

    @Test
    public void create_a_single_submission() throws Exception {
        //prep data
        String submissionJson = "{\n" +
                "  \"team\": {\n" +
                "    \"name\": \"subs.team-1234\"\n" +
                "  },\n" +
                "  \"title\": \"My first submission\"\n" +
                "}";


        //do request and verify
        this.mockMvc.perform(
                post(documentationHelper.collectionRoot(Submission.class))
                        .accept(RestMediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(submissionJson)
        ).andExpect(status().isCreated())
                .andDo(
                        document("create-one-submission",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionLinks(),
                                submissionResponseFields()
                        )
                );
    }

    @Test
    public void patch_a_single_submission() throws Exception {
        //prep data
        Submission submission = addSubmissionToDb("My 1st submission", SubmissionStatus.Draft);

        //prep request url
        Link resourceLink = repositoryEntityLinks
                .linkToSingleResource(submission);

        //prep data
        String patchJson = "{\"title\": \"My first submission\"}";


        //do request and verify
        this.mockMvc.perform(
                patch(resourceLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        ).andExpect(status().isOk())
                .andDo(
                        document("patch-one-submission",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionLinks(),
                                submissionResponseFields()
                        )
                );
    }

    @Test
    public void update_a_single_submission() throws Exception {
        //prep data
        Submission submission = addSubmissionToDb("My 1st submission", SubmissionStatus.Draft);

        //prep request url
        Link resourceLink = repositoryEntityLinks
                .linkToSingleResource(submission);

        //prep data
        String replacementJson = "{\n" +
                "  \"team\": {\n" +
                "    \"name\": \"subs.team-1234\"\n" +
                "  },\n" +
                "  \"title\": \"My first submission\",\n" +
                "  \"status\": \"Draft\"\n" +
                "}";


        //do request and verify
        this.mockMvc.perform(
                put(resourceLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(replacementJson)
        ).andExpect(status().isOk())
                .andDo(
                        document("update-one-submission",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionLinks(),
                                submissionResponseFields()
                        )
                );
    }

    @Test
    public void submit_a_single_submission() throws Exception {
        //prep data
        Submission submission = addSubmissionToDb("My 1st submission", SubmissionStatus.Draft);

        //prep request url
        Link resourceLink = repositoryEntityLinks
                .linkToSingleResource(submission);

        //prep data
        String patchJson = "{\"status\": \"Submitted\"}";


        //do request and verify
        this.mockMvc.perform(
                patch(resourceLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        ).andExpect(status().isOk())
                .andDo(
                        document("submit-one-submission",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                submissionLinks(),
                                submissionResponseFields()
                        )
                );
    }

    private ResponseFieldsSnippet submissionResponseFields() {
        return responseFields(
                fieldWithPath("id").ignored(),
                subsectionWithPath("team").ignored(),
                fieldWithPath("title").ignored(),
                fieldWithPath("status").ignored(),
                subsectionWithPath("_actions").description("<<submission-actions,Actions>> available"),
                subsectionWithPath("_audit").ignored(),
                DocumentationHelper.linksResponseField()
        );
    }

    @Test
    public void delete_a_single_submission() throws Exception {
        //prep data
        Submission submission = addSubmissionToDb("My 1st submission", SubmissionStatus.Draft);

        //prep request url
        Link resourceLink = repositoryEntityLinks
                .linkToSingleResource(submission);


        //do request and verify
        this.mockMvc.perform(
                delete(resourceLink.getHref())
        ).andExpect(status().isNoContent())
                .andDo(
                        document("delete-one-submission",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void summarise_statuses_for_user() throws Exception {


        //prep data
        Map<SubmissionStatus, Integer> statusCounts = ImmutableMap.<SubmissionStatus, Integer>builder()
                .put(SubmissionStatus.Completed, 4)
                .put(SubmissionStatus.Processing, 1)
                .put(SubmissionStatus.Draft, 1)
                .build();

        statusCounts.entrySet().stream()
                .forEach(entry ->
                        IntStream.generate(() -> 1)
                                .limit(entry.getValue())
                                .forEach(
                                        i -> addSubmissionToDb("", entry.getKey())
                                )
                );

        //prep request url

        Link searchLink = linkTo(
                methodOn(SubmissionController.class)
                        .userSubmissionStatusSummary()

        ).withSelfRel();


        //do request and verify
        this.mockMvc.perform(
                get(searchLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("submission-status-summary-for-user",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("self").ignored()
                                ),
                                responseFields(
                                        fieldWithPath("content." + SubmissionStatus.Completed.name()).description("Number of completed submissions"),
                                        fieldWithPath("content." + SubmissionStatus.Processing.name()).description("Number of submissions currently being processed"),
                                        fieldWithPath("content." + SubmissionStatus.Draft.name()).description("Number of submissions in draft"),
                                        DocumentationHelper.linksResponseField()
                                )
                        )
                );
    }

    private Submission addSubmissionToDb(String title, SubmissionStatus statusEnum) {
        Submission s = new Submission();

        s.setTitle(title);
        s.setStatus(statusEnum);
        s.setTeam(Team.of(TestUserAndTeamNames.TEAM_NAME));

        return submissionMongoRepository.insert(s);
    }

    /**
     * Standard package for resources
     *
     * Link relations - maintain manuallly?
     * List _resources_ / OR Search resources
     * Get a single _resource_
     * Create a _resource_
     * Update a _resource_
     * Delete a _resource_
     *
     * Schema
     */


}
