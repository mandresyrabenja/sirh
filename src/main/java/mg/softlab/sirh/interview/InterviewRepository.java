package mg.softlab.sirh.interview;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByJobOffer(JobOffer offer);
}
