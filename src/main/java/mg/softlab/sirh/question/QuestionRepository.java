package mg.softlab.sirh.question;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByJobOffer(JobOffer jobOffer);
}
