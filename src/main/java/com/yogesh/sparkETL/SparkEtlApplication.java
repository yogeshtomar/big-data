package com.yogesh.sparkETL;

import com.yogesh.sparkETL.model.Employee;
import com.yogesh.sparkETL.service.EmployeeService;
import org.apache.spark.sql.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import static org.apache.spark.sql.functions.*;

import java.util.List;

@SpringBootApplication
public class SparkEtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkEtlApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(EmployeeService employeeService) {
		return args -> {
			// Spark transformation logic (copy from SparkTransformation.java)
			SparkSession spark = SparkSession.builder()
					.appName("SparkEtlApplication")
					.config("spark.master", "local[*]")
					.getOrCreate();
			//------------ Change this line --------------------------------------
			String csvFilePath = "src/main/resources/data/Employees.csv";
			Dataset<Row> rawData = spark.read()
					.option("header", "true")  // Assuming the first row is a header
					.option("inferSchema", "true")  // Inferring the schema
					.option("sep", ",")
					.csv(csvFilePath);

			rawData.show();

			Dataset<Row> processedData = rawData
					.withColumn("Experience", expr("cast(datediff(current_date(), HiringDate) / 365.0 as int)"))
					.withColumn("SalaryWithBonus", col("Salary").plus(expr("cast(Experience as int) * 500")))
					.withColumn("DepartmentUpperCase", upper(col("Department")))
					.withColumn("HiringDate", to_date(col("HiringDate"), "yyyy-MM-dd"));



			// Convert processedData to a list of Employee entities and save to MongoDB

			List<Employee> employees = processedData.as(Encoders.bean(Employee.class)).collectAsList();
			employeeService.saveAllEmployees(employees);

			System.out.println(employeeService.getAllEmployees());
		};
	}
}
