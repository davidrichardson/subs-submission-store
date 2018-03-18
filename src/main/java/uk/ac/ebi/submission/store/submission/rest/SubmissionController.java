package uk.ac.ebi.submission.store.submission.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.submission.store.common.IdentifiableResourceSelfLinker;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.user.CurrentUserService;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
public class SubmissionController {

    @NonNull
    private SubmissionMongoRepository submissionMongoRepository;

    @NonNull
    private PagedResourcesAssembler<Submission> pagedResourcesAssembler;
    @NonNull
    private CurrentUserService currentUserService;
    @NonNull
    private SubmissionResourceProcessor submissionResourceProcessor;
    @NonNull
    private IdentifiableResourceSelfLinker<Submission> identifiableResourceSelfLinker;

    @RequestMapping(method = RequestMethod.GET, value = "/user/submissions")
    public
    ResponseEntity<PagedResources<Resource<Submission>>> userSubmissions(Pageable pageable) {
        List<String> teamNamesForUser = currentUserService.userTeams();

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

    @RequestMapping(method = RequestMethod.GET, value = "/user/submissions/statusSummary")
    public @ResponseBody
    ResponseEntity<Resource<Map<String, Integer>>> userSubmissionStatusSummary() {
        List<String> userTeamNames = currentUserService.userTeams();

        Map<String, Integer> statusCounts = submissionMongoRepository.statusCountsByTeam(userTeamNames);

        Link selfLink = linkTo(methodOn(this.getClass()).userSubmissionStatusSummary()).withSelfRel();

        return ResponseEntity.ok(
                new Resource<>(statusCounts, selfLink)
        );
    }


}
