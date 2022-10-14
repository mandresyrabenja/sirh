package mg.softlab.sirh.employee;

import mg.softlab.sirh.department.Department;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.person.Person;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;

/**
 * JPA specifications for employee
 */
public class EmployeeSpecifications {

    public static Specification<Employee> hasDepartment(Long departmentId) {
        if(null == departmentId) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Job, Employee> job = root.join("job");
            Join<Department, Job> department = job.join("department");
            return cb.equal(department.<Long>get("id"), departmentId);
        };
    }

    public static Specification<Employee> hasNameLike(String name) {
        if(null == name) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Person, Employee> person = root.join("person");
            // Like firstname + " " + name
            Expression<String> expr1 = cb.concat(person.<String>get("firstname"), " ");
            expr1 = cb.lower( cb.concat(expr1, person.<String>get("name")) );

            // Like name + " " + firstname
            Expression<String> expr2 = cb.concat(person.<String>get("name"), " ");
            expr2 = cb.lower( cb.concat(expr2, person.<String>get("firstname")) );

            return cb.or(
                    cb.like(expr1, "%" + name.toLowerCase() + "%"),
                    cb.like(expr2, "%" + name.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Employee> hasJobLike(String jobName) {
        if(null == jobName) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Job, Employee> job = root.join("job");
            return cb.like(cb.lower(job.<String>get("name")), "%" + jobName.toLowerCase() + "%");
        };
    }
}
