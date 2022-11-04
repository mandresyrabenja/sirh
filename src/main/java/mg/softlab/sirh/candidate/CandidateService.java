package mg.softlab.sirh.candidate;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.email.EmailService;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import mg.softlab.sirh.person.Person;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final JobOfferService jobOfferService;
    private final EmailService emailService;

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
        if("age".equalsIgnoreCase(sortValue)) {
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            return candidateRepository.findByJobOffer(jobOffer)
                    .stream()
                    .sorted(Comparator.comparingInt(c -> c.getPerson().getAge()))
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Le valeur du paramètre criteria acceptable sont 'degree', " +
                "'experience' et 'age'. Vous avez entré " + sortValue);
    }

    public List<Candidate> findCandidateByJobOffer(JobOffer jobOffer) {
        return candidateRepository.findByJobOffer(jobOffer);
    }

    public Candidate createCandidate(JobOffer jobOffer, Person person) {

        Candidate candidate = new Candidate();
        candidate.setPerson(person);
        candidate.setJobOffer(jobOffer);
        candidate.setIsChoosen(false);

        return candidateRepository.save(candidate);
    }

    public Candidate findById(Long id) {
        return candidateRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucune candidature n'a " + id + " comme ID")
        );
    }

    @Transactional
    public void chooseCandidate(Long candidateId) {
        Candidate candidate = findById(candidateId);
        candidate.setIsChoosen(true);

        sendAcceptationEmail(candidate);
    }

    private void sendAcceptationEmail(Candidate candidate) {
        String subject = "Acceptation de candidature pour le poste de %s";
        String body = """
                Bonjour %s %s,
                        
                A la suite de nos entretiens, nous avons le plaisir de vous informer que votre candidature au poste de %s, a été retenue.
                        
                Nous vous remercions de bien vouloir prendre contact avec notre service recrutement le plus rapidement possible afin que nous puissions nous rencontrer.
                Nous nous tenons à votre disposition  pour tout renseignement complémentaire qu’il vous plairait de nous demander.
                        
                Nous vous souhaitons une belle journée.

                Softlab SARLU
                contact@softlab.mg
                +261 34 83 088 71
                """;

        Person person = candidate.getPerson();
        JobOffer jobOffer = candidate.getJobOffer();

        emailService.sendSimpleMessage(person.getEmail(),
                String.format(subject, jobOffer.getTitle()),
                String.format(body,
                        person.getFirstname(), person.getName(),
                        jobOffer.getTitle()
                        )
            );
    }

    public List<Candidate> findChoosenCandidates(Long offerId) {
        JobOffer jobOffer = jobOfferService.findById(offerId);
        return candidateRepository.findByIsChoosenTrueAndJobOffer(jobOffer);
    }

    public boolean existsById(Long id) {
        return candidateRepository.existsById(id);
    }
}
