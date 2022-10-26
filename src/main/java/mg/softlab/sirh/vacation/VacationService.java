package mg.softlab.sirh.vacation;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.employee.Employee;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
public class VacationService {
    private final VacationRepository vacationRepository;

    /**
     * Initialiser le congé d'un employé si l'employé n'a pris pas aucun congé avant
     * @param employee L'employé
     */
    @Transactional
    public void initEmployeeVacation(Employee employee) {
        LocalDate vacationOrigin = employee.getHiringDate().plusYears(1);
        double restOfDayOff = ((double)Period.between(vacationOrigin, LocalDate.now()).getMonths()) / 2.5;
        Vacation vacation = Vacation.builder()
                .employee(employee)
                .reason("Initialisation du congé")
                .startDate(vacationOrigin)
                .endDate(vacationOrigin)
                .rest((byte) 0)
                .build();
        vacationRepository.save(vacation);
        vacation.getEmployee().setVacationOrigin(vacationOrigin);
    }

    /**
     * Ajouter des jours de congé d'un employé
     * @param vacation Jour de congé de l'employé
     * @return Entité contenant le jour de congé ajouté
     */
    public Vacation addEmployeeVacation(Vacation vacation) {
        Employee employee = vacation.getEmployee();

        /* Contrôle unitaire des données */
        // L'employé doît travaillé pendant au moins une année pour avoir droit à un congé
        if(Period.between(employee.getHiringDate(), vacation.getStartDate()).getYears() < 1) {
            throw new IllegalStateException("L'ancienneté de l'employé numero " + employee.getId() +
                    " doît depassé une année. Sa date d'embauche: " + employee.getHiringDate());
        }
        // La date de début du jour de congé doît être apres ou égale à la date d'aujourd'hui
        if(LocalDate.now().isAfter(vacation.getStartDate())) {
            throw new IllegalStateException("La date de début du congé doît être apres ou égale à la date " +
                    "d'aujourd'hui. Date de début: " + vacation.getStartDate());
        }
        // La date de début du jour de congé doît être avant ou égale à la date de fin du jour de congé
        if(vacation.getStartDate().isAfter(vacation.getEndDate())) {
            throw new IllegalStateException(
                    "La date de début du jour de congé doît être avant ou égale à la date de fin du jour de congé." +
                    " Date de début: " + vacation.getStartDate() + " -- Date de fin:" + vacation.getEndDate());
        }

        vacation.setRest(calculateRest(vacation));
        return vacationRepository.save(vacation);
    }

    /**
     * Calculer le nombre des jours de congé restant de l'employé après avoir pris le congé <b>vacation</b>
     * @param vacation Le congé que l'employé veut prendre
     * @return le nombre des jours de congé restant de l'employé
     */
    private byte calculateRest(Vacation vacation) {

        //Si l'employé n'a pas encore pris aucun congé
        if(vacationRepository.countByEmployee(vacation.getEmployee()) == 0){
            initEmployeeVacation(vacation.getEmployee());
            return calculateRest(vacation);
        }

        return 0;
    }

    public List<Vacation> getEmployeeVacations(Employee employee) {
        return vacationRepository.findByEmployeeOrderByStartDateDesc(employee);
    }
}
