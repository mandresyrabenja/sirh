package mg.softlab.sirh.vacation;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.employee.Employee;
import mg.softlab.sirh.employee.EmployeeService;
import mg.softlab.sirh.holiday.Holiday;
import mg.softlab.sirh.holiday.HolidayService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class VacationService {
    private final VacationRepository vacationRepository;
    private final HolidayService holidayService;
    private final EmployeeService employeeService;

    /**
     * Initialiser le congé d'un employé si l'employé n'a pris aucun congé avant
     * @param employee L'employé
     */
    public void initEmployeeVacation(Employee employee) {
        /*
         L'employé doit avoir au moins 1 ans d'ancienneté
         date d'origine des congés = date d'embauche + 1 ans
         */
        LocalDate vacationOrigin = employee.getHiringDate().plusYears(1);

        /*
        L'employé a droit à 2.5 jours de congé/mois calendaire
        nb congé restant = nb mois calendaire entre aujourd'hui et la date d'origine des congés * 2.5 jours
        */
        double restOfDayOff = getNbCalendarMonth(vacationOrigin) * 2.5;

        /*
         Le maximum de congé qu'un employé peut prendre est de 90jours
         si le nombre des congés restant depasse le 90jours: date d'origine du congé = date d'origine du congé + 3 ans
         */
        if(restOfDayOff > 90) {
            vacationOrigin = vacationOrigin.plusYears(3);
        }

        /*
        Initialisation du congé de l'employé
         */
        Vacation vacation = Vacation.builder()
                .employee(employee)
                .reason("Initialisation du congé")
                .startDate(vacationOrigin)
                .endDate(vacationOrigin)
                .rest((byte) 0)
                .build();
        vacationRepository.save(vacation);
        employeeService.updateEmployee(vacation.getEmployee().getId(), vacationOrigin);
    }

    /**
     * Avoir le nombre des mois calendaire entre une date entrée et la date d'aujourd'hui
     * @param date Date à partir de laquelle on compte
     * @return le nombre des mois calendaire entre le parametre "<b>date</b>" et la date d'aujourd'hui
     */
    private long getNbCalendarMonth(LocalDate date) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        return ChronoUnit.MONTHS.between(firstDayOfMonth, LocalDate.now());
    }

    /**
     * Ajouter des jours de congé pour un employé
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

        double rest = calculateRest(vacation);
        if(rest < 0) {
            throw new IllegalStateException(
                    String.format(
                        """
                        L'employé numero %d n'a plus assez de congé restant.
                        Nombre de jours de congé qu'il veut prendre: %d
                        Nombre de jours de congé restant: %.1f
                        """
                        , employee.getId()
                        , getWorkingDays(vacation.getStartDate(), vacation.getEndDate())
                        , getRestDayOff(employee)
                    )
            );
        }
        vacation.setRest((byte) rest);
        return vacationRepository.save(vacation);
    }

    /**
     * Calculer le nombre des jours de congé restant de l'employé après avoir pris le congé <b>vacation</b>
     * @param vacation Le congé que l'employé veut prendre
     * @return le nombre des jours de congé restant de l'employé
     */
    @Transactional
    public double calculateRest(Vacation vacation) {

        //Si l'employé n'a pas encore pris aucun congé
        Employee employee = vacation.getEmployee();
        if(vacationRepository.countByEmployee(employee) == 0){
            initEmployeeVacation(employee);
        }

        // Le dernier congé pris par l'employé
        Vacation lastVacation = getEmployeeLastVacation(employee);
        // Le reste des congé non pris par l'employé
        double restOfDayOff = getRestDayOff(employee);

        /*
        Le maximum de congé qu'un employé peut prendre est de 90jours
        si le congé restant depasse le 90 jours: date d'origine du congé = date d'origine du congé + 3 ans
        */
        if(restOfDayOff > 90) {
            employee.setVacationOrigin(
                    lastVacation.getStartDate()
                            .plusYears(3)
                            .minusMonths((long) (lastVacation.getRest()/2.5))
            );
            Vacation initVacation = Vacation.builder()
                    .rest((byte) 0)
                    .startDate(employee.getVacationOrigin())
                    .endDate(employee.getVacationOrigin())
                    .reason("3 ans ecoulé")
                    .employee(employee)
                    .build();
            vacationRepository.save(initVacation);
            restOfDayOff = getRestDayOff(employee);
        }

        /*
        Seul les jours ouvrables sont compté dans les jours de congé.
        */
        long nbDays = getWorkingDays(vacation.getStartDate(), vacation.getEndDate());

        /*
        Calcule du nombre des jours des congés restant
        nb jours de congé restant = reste des congés non pris lors du dernier congé
        + ( mois_calendaire(aujourd'hui, début du dernier congé) * 2.5 jours)
        - nb jours du nouvelle congé qu'il/elle va prendre
         */
        return restOfDayOff - nbDays;
    }

    /**
     * Avoir le nombre des jours ouvrables entre 2 dates.
     * Les jours non ouvrable sont les dimanches et les jours feriés.<br>
     * jour ouvrable = diff(date debut, date fin) - nb dimanches - nb jour feriés
     * @param startDate Date de début
     * @param endDate Date de fin
     * @return Nombre des jours ouvrables entre la date de début et la date de fin
     */
    private long getWorkingDays(LocalDate startDate, LocalDate endDate) {
        // Nombre des dimanches entre la date de debut et la date de fin du congé
        int nbSunday = getNbSunday(startDate, endDate);
        // Nombre des jours feriés
        int nbHoliday = getNbHolidays(startDate, endDate);

        // Nombre des jours ouvrable
        long nbDays = ChronoUnit.DAYS.between(startDate, endDate);
        nbDays -= (nbSunday + nbHoliday);
        return nbDays;
    }

    /**
     * Avoir le nombre des jours feriés entre 2 date.<br>
     * <b>Note:</b> On ne compte les jours feriés qui tombent en dimanche.
     * @param startDate Date de début
     * @param endDate Date de fin
     * @return Le nombre des jours feriés entre la date de début et la date de fin
     */
    private int getNbHolidays(LocalDate startDate, LocalDate endDate) {
        List<Holiday> holidays = holidayService.findAllHolidays();
        int nbHoliday = 0;
        for(Holiday holiday : holidays) {
            if( (startDate.compareTo(holiday.getDate()) * holiday.getDate().compareTo(endDate) >= 0)
                && (holiday.getDate().getDayOfWeek().getValue() != DayOfWeek.SUNDAY.getValue()) ) {
                nbHoliday++;
            }
        }
        return nbHoliday;
    }

    /**
     * Avoir le nombre des dimanches entre 2 date
     * @param startDate Date de début inclusive
     * @param endDate Date de fin exlusive
     * @return le nombre des dimanches entre la date de debut et la date de fin
     */
    private int getNbSunday(LocalDate startDate, LocalDate endDate) {
        int nbSunday = 0;
        long nbDays = ChronoUnit.DAYS.between(startDate, endDate);
        LocalDate d = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
        for(int i = 0; i < nbDays; i++) {
            if(d.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue()) {
                nbSunday++;
            }
            d = d.plusDays(1);
        }
        return nbSunday;
    }


    /**
     * Calcule du nombre des jours des congés non pris par un employé.<br>
     * nb jours de congé non pris = reste des congés non pris lors du dernier congé
     * + ( mois_calendaire(aujourd'hui, début du dernier congé) * 2.5 jours)
     * @param employee L'employé
     * @return Le nombre des jours des congés non pris par l'employé
     */
    private double getRestDayOff(Employee employee) {
        Vacation lastVacation = getEmployeeLastVacation(employee);
        long nbCalendarMonth = getNbCalendarMonth(lastVacation.getStartDate());
        return lastVacation.getRest() + (nbCalendarMonth * 2.5);
    }

    private Vacation getEmployeeLastVacation(Employee employee) {
        return vacationRepository.findByEmployeeOrderByStartDateDesc(
                employee,
                PageRequest.of(0, 1)
        ).get(0);
    }

    public List<Vacation> getEmployeeVacations(Employee employee) {
        return vacationRepository.findByEmployeeOrderByStartDateDesc(employee, PageRequest.of(0, 200));
    }
}
