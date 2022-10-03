package mg.softlab.sirh.candidate;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByJobOffer(JobOffer jobOffer);
}
