package mg.softlab.sirh.experience;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.degree.Degree;
import mg.softlab.sirh.degreeCategory.DegreeCategory;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/experiences")
@RequiredArgsConstructor
@Slf4j
public class ExperienceController {
    private final ExperienceService experienceService;
    private final PersonService personService;

    @PostMapping
    public ResponseEntity<String> addPersonExperience(@RequestParam Long personId,
                                                      @RequestParam String title,
                                                      @RequestParam String company,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
    {
        try{
            Person person = personService.findById(personId);

            Experience experience = new Experience();
            experience.setPerson(person);
            experience.setTitle(title);
            experience.setCompany(company);
            experience.setStartDate(startDate);
            experience.setEndDate(endDate);

            experienceService.addExperience(experience);
            String msg = "Expérience du personne numero " + personId + " ajouté avec succès";
            log.info(msg);
            return ResponseEntity.ok(msg);
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
