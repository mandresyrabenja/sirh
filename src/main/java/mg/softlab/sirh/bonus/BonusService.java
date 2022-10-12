package mg.softlab.sirh.bonus;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BonusService {
    private final BonusRepository bonusRepository;

    public void addCandidateBonus(Bonus bonus) {
        bonusRepository.save(bonus);
    }
}
