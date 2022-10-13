package mg.softlab.sirh.employmentContract;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.employee.Employee;
import mg.softlab.sirh.employmentContract.category.EmploymentContractCategory;
import mg.softlab.sirh.job.Job;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "employment_contract")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentContract {
    @Id
    @SequenceGenerator(
            name = "sequence_contract",
            sequenceName = "sequence_contract"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_contract"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonBackReference("employee_contract")
    private Employee employee;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    /**
     * Salaire de base mensuel
     */
    @Column(nullable = false)
    private Double baseSalary;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_category_id")
    private EmploymentContractCategory category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @Column(nullable = true)
    private String assignment;
}
