package mg.softlab.sirh.experience;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;

    public void addExperience(Experience experience) {
        experienceRepository.save(experience);
    }
}
