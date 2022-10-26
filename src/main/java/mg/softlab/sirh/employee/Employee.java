package mg.softlab.sirh.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.employmentContract.EmploymentContract;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.vacation.Vacation;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @SequenceGenerator(
            name = "sequence_employee",
            sequenceName = "sequence_employee",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_employee"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @OneToOne
    private Person person;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
    private Job job;

    /**
     * Matricule CNAPS
     */
    @Column(nullable = true)
    private String cnaps;

    /**
     * Matricule OSTIE
     */
    @Column(nullable = true)
    private String ostie;

    @Column(nullable = false)
    private LocalDate hiringDate;

    @Column(nullable = false)
    private LocalDate vacationOrigin;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "employee")
    @JsonManagedReference("employee_contract")
    private Collection<EmploymentContract> contracts;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "employee")
    @JsonManagedReference("employee_vacation")
    private Collection<Vacation> vacations;
}
