package mg.softlab.sirh.question;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByJobOffer(JobOffer jobOffer, Pageable pageable);
}
