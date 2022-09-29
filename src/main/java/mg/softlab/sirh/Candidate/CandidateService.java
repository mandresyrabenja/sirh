package mg.softlab.sirh.Candidate;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final PersonService personService;

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
