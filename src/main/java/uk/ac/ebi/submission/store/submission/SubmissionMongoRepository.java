package uk.ac.ebi.submission.store.submission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


//TODO read/write by owner or admin
public interface SubmissionMongoRepository extends MongoRepository<Submission,String>{

    Page<Submission> findByTeamName(Pageable page);
}
