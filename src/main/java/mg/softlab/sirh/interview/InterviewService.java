package mg.softlab.sirh.interview;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;

    public Interview createInterview(Candidate candidate, JobOffer jobOffer, LocalDateTime dateTime) {
        Interview interview = new Interview();
        interview.setCandidate(candidate);
        interview.setJobOffer(jobOffer);
        interview.setDateTime(dateTime);

        return interviewRepository.save(interview);
    }

    public List<Interview> findJobInterviews(JobOffer offer) {
        return interviewRepository.findByJobOffer(offer);
    }
}
