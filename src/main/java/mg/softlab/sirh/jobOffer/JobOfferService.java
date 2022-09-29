package mg.softlab.sirh.jobOffer;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.job.Job;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public void createJobOffer(JobOffer jobOffer) {
        jobOfferRepository.save(jobOffer);
    }
}
