package uk.ac.ebi.submission.store.submission.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.submission.store.common.IdentifiableResourceSelfLinker;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.user.UserTeamService;

import java.util.List;


@RepositoryRestController
@RequiredArgsConstructor
public class SubmissionController {

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @NonNull
    private PagedResourcesAssembler<Submission> pagedResourcesAssembler;
    @NonNull
    private UserTeamService userTeamService;
    @NonNull
    private SubmissionResourceProcessor submissionResourceProcessor;
    @NonNull
    private IdentifiableResourceSelfLinker<Submission> identifiableResourceSelfLinker;

    @RequestMapping(method = RequestMethod.GET, value = "/submissions/search/user")
    public @ResponseBody
    ResponseEntity<?> userSubmissions(Pageable pageable) {
        List<String> teamNamesForUser = userTeamService.userTeams();

        Page<Submission> submissions = submissionMongoRepository.findByTeamNameInOrderByCreatedByDesc(teamNamesForUser, pageable);

        PagedResources<Resource<Submission>> resources = pagedResourcesAssembler.toResource(submissions, entity -> {
            Resource<Submission> resource = new Resource(entity);

            identifiableResourceSelfLinker.addSelfLinks(resource);
            submissionResourceProcessor.process(resource);

            return resource;
        }
        );


        return ResponseEntity.ok(resources);
    }


}
