package mg.softlab.sirh.question;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question addJobOfferQuestion(JobOffer jobOffer, String question, Short maxMark) {
        Question entity = new Question();
        entity.setJobOffer(jobOffer);
        entity.setDescription(question);
        entity.setMaxMark(maxMark);
        return questionRepository.save(entity);
    }

    public Page<Question> getJobOfferQuestion(JobOffer jobOffer, Pageable pageable) {
        return questionRepository.findByJobOffer(jobOffer, pageable);
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucune question n'a " + id + " comme ID"));
    }
}
