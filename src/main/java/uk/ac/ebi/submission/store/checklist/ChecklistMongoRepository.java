package uk.ac.ebi.submission.store.checklist;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChecklistMongoRepository extends MongoRepository<Checklist, String> {
}
