package mg.softlab.sirh.degree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.degreeCategory.DegreeCategory;
import mg.softlab.sirh.degreeCategory.DegreeCategoryService;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/degrees")
@RequiredArgsConstructor
@Slf4j
public class DegreeController {
    private final DegreeService degreeService;
    private final PersonService personService;
    private final DegreeCategoryService degreeCategoryService;

    @PostMapping
    public ResponseEntity<String> addPersonDegree(@RequestParam Long personId,
                                                  @RequestParam Long degreeCatId,
                                                  @RequestParam String title,
                                                  @RequestParam String establishment,
                                                  @RequestParam Integer year) {
        try{
            Person person = personService.findById(personId);
            DegreeCategory degreeCategory = degreeCategoryService.findById(degreeCatId);

            Degree degree = new Degree();
            degree.setPerson(person);
            degree.setCategory(degreeCategory);
            degree.setTitle(title);
            degree.setEstablishment(establishment);
            degree.setYear(year);

            degreeService.addPersonDegree(degree);
            String msg = "Diplôme du personne numero " + personId + " ajouté avec succès";
            log.info(msg);
            return ResponseEntity.ok(msg);
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
