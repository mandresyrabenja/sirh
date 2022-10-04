package mg.softlab.sirh.candidate;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.person.Person;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    /**
     * Trier les candidats d'une offre d'emploi par dimplôme, expérience ou age
     * @param jobOfferId ID de l'offre d'emploi<br>
     * @param sortValue chaîne de caractère :<br>
     *                 <ul>
     *                  <li><b>"degree"</b> pour diplôme</li>
     *                  <li><b>"experience"</b> pour l'expérience</li>
     *                  <li><b>"age"</b> pour le plus jeune au plus âgé</li>
     *                 </ul>
     * @return Liste des candidats de l'offre d'emploi triée par dimplôme, expérience ou âge
     */
    public List<Candidate> sortCandidate(Long jobOfferId, String sortValue) {
        if("degree".equalsIgnoreCase(sortValue)) {
            return candidateRepository.orderByDegree(jobOfferId);
        }
        if("experience".equalsIgnoreCase(sortValue)) {
            return candidateRepository.orderByExperience(jobOfferId)
                    .stream()
                    .map((experience) -> {
                                BigInteger candidateId = (BigInteger) experience[0];
                                return candidateRepository.findById(candidateId.longValue())
                                    .orElseThrow( () -> new IllegalStateException(
                                        "Aucun candidature n'a " + candidateId + " comme ID") );
                            }
                    )
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Le valeur du paramètre criteria acceptable sont 'degree', " +
                "'experience' et 'age'. Vous avez entré " + sortValue);
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
