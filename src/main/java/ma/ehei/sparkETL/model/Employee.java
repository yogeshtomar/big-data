package ma.ehei.sparkETL.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String employeeID;

    private String firstName;
    private String lastName;
    private int Salary;
    private String department;
    private LocalDate hiringDate;
    private int salaryWithBonus;
    private String departmentUpperCase;
    private double experience;


}
