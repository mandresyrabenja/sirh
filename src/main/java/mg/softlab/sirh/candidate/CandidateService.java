package mg.softlab.sirh.candidate;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final PersonService personService;

    public List<Candidate> findCandidateByJobOffer(JobOffer jobOffer) {
        return candidateRepository.findByJobOffer(jobOffer);
    }

    @Transactional
    public void createCandidate(JobOffer jobOffer, Person person) {
        personService.createPerson(person);

        Candidate candidate = new Candidate();
        candidate.setPerson(person);
        candidate.setJobOffer(jobOffer);
        candidate.setIsChoosen(false);

        candidateRepository.save(candidate);
    }
}
