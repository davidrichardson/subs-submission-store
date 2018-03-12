package uk.ac.ebi.submission.store.mockmvc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.submission.DocumentationProducer;
import uk.ac.ebi.submission.store.SubsSubmissionStoreApplication;
import uk.ac.ebi.submission.store.TestUserAndTeamNames;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubsSubmissionStoreApplication.class)
@WithMockUser(username = "usi_admin_user", roles = {TestUserAndTeamNames.TEAM_NAME, TestUserAndTeamNames.ADMIN_TEAM_NAME})
@Category(DocumentationProducer.class)
public class SubmissionDocumentApiTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DocumentationHelper.SNIPPET_OUTPUT_DIR);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentationHelper documentationHelper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockMvcRestDocumentationConfigurer docConfig = documentationHelper.docConfig(restDocumentation);
        this.mockMvc = DocumentationHelper.mockMvc(this.context, docConfig);
    }

    @Test
    public void test(){

    }

}
