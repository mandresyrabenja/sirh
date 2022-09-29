package mg.softlab.sirh.jobOffer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public void createJobOffer(JobOffer jobOffer) {
        jobOfferRepository.save(jobOffer);
    }

    public List<JobOffer> getAvailableJobOffers() {
        return jobOfferRepository.findByIsDone(false);
    }

    public JobOffer findById(Long id) {
        return jobOfferRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucune offre d'emploi n'a " + id + " comme ID")
        );
    }
}
