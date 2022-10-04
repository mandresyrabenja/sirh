package mg.softlab.sirh.degree;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeService {
    private final DegreeRepository degreeRepository;

    public void addPersonDegree(Degree degree) {
        degreeRepository.save(degree);
    }

}
