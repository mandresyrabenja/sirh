package mg.softlab.sirh.candidate;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.person.Person;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public List<Candidate> sortCandidate(Long jobOfferId, String sortValue) {
        if("degree".equalsIgnoreCase(sortValue)) {
            return candidateRepository.orderByDegree(jobOfferId);
        }
        return null;
    }

    public List<Candidate> findCandidateByJobOffer(JobOffer jobOffer) {
        return candidateRepository.findByJobOffer(jobOffer);
    }

    @Transactional
    public void createCandidate(JobOffer jobOffer, Person person) {

        Candidate candidate = new Candidate();
        candidate.setPerson(person);
        candidate.setJobOffer(jobOffer);
        candidate.setIsChoosen(false);

        candidateRepository.save(candidate);
    }
}
