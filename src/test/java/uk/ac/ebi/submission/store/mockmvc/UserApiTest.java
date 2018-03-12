package uk.ac.ebi.submission.store.mockmvc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.hateoas.Link;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.submission.DocumentationProducer;
import uk.ac.ebi.submission.SubmissionsApplication;
import uk.ac.ebi.submission.store.TestUserAndTeamNames;
import uk.ac.ebi.submission.store.user.CurrentUserService;
import uk.ac.ebi.submission.store.user.UserController;
import uk.ac.ebi.tsc.aap.client.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmissionsApplication.class)
@WithMockUser(username = "subs_user", roles = {TestUserAndTeamNames.TEAM_NAME})
@Category(DocumentationProducer.class)
public class UserApiTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DocumentationHelper.SNIPPET_OUTPUT_DIR);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentationHelper documentationHelper;

    @MockBean
    private CurrentUserService currentUserService;

    private MockMvc mockMvc;

    private User user;
    private List<String> teamNames;

    @Before
    public void setUp() {
        MockMvcRestDocumentationConfigurer docConfig = documentationHelper.docConfig(restDocumentation);
        this.mockMvc = DocumentationHelper.mockMvc(this.context, docConfig);

        this.user = new User(
                "userName",
                "alice@example.org",
                "usr-1234",
                "Alice Example",
                Collections.emptySet()
        );

        this.teamNames = Arrays.asList(
                "subs.team-1234",
                "subs.team-xyz"
        );

    }

    @Test
    public void current_user() throws Exception {

        when(currentUserService.getCurrentUser()).thenReturn(this.user);

        //prep request url

        Link userLink = linkTo(
                methodOn(UserController.class)
                        .currentUser()

        ).withSelfRel();


        //do request and verify
        this.mockMvc.perform(
                get(userLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("current-user",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("curies").ignored(),
                                        linkWithRel("self").ignored(),
                                        linkWithRel("subs:teams").description("Teams for the user"),
                                        linkWithRel("subs:user").ignored()
                                ),
                                responseFields(
                                        fieldWithPath("userName").description("User name of user"),
                                        fieldWithPath("email").description("Contact e-mail for user"),
                                        fieldWithPath("userReference").description("User reference"),
                                        fieldWithPath("fullName").description("Full name for user"),
                                        DocumentationHelper.linksResponseField()
                                )
                        )
                );

    }

    @Test
    public void current_user_teams() throws Exception {

        when(currentUserService.userTeams()).thenReturn(this.teamNames);

        //prep request url

        Link userLink = linkTo(
                methodOn(UserController.class)
                        .currentUserTeams()

        ).withSelfRel();


        //do request and verify
        this.mockMvc.perform(
                get(userLink.getHref())
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("current-user-teams",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("curies").ignored(),
                                        linkWithRel("self").ignored(),
                                        linkWithRel("subs:teams").ignored()
                                ),
                                responseFields(
                                        subsectionWithPath("teams").description("List of teams"),
                                        DocumentationHelper.linksResponseField()
                                )
                        )
                );

    }
}