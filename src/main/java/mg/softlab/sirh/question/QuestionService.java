package mg.softlab.sirh.question;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Question> getJobOfferQuestion(JobOffer jobOffer) {
        return questionRepository.findByJobOffer(jobOffer);
    }
}
