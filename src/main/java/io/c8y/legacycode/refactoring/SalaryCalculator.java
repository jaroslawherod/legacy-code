package io.c8y.legacycode.refactoring;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class SalaryCalculator {
    private final InitialContext context;

    public SalaryCalculator() throws NamingException {
        this.context = new InitialContext();
    }

    public SalaryCalculator(InitialContext context) {
        this.context = context;
    }

    public double calculateSalary(Long employeeId, int year, int month) throws NamingException, SQLException {
        Context envContext = (Context) context.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/datasource");

        Connection connection = ds.getConnection();
        try {
            final PreparedStatement getEmployee = connection.prepareStatement("select * from employee where id = ? ");
            final PreparedStatement getCompanyResult = connection.prepareStatement("select result from company_result where year = ? and month = ? ");
            try {
                getEmployee.setLong(1, employeeId);
                final ResultSet employeeResultSet = getEmployee.executeQuery();

                getCompanyResult.setInt(1, year);
                getCompanyResult.setInt(2, month);
                final ResultSet companyResultResultSet = getCompanyResult.executeQuery();
                try {
                    if (!employeeResultSet.next()) {
                        throw new NoSuchElementException("Can't find user with id " + employeeId);
                    }
                    if (!companyResultResultSet.next()) {
                        throw new NoSuchElementException(String.format("Can't company result for year %d and month %d", year, month));
                    }
                    double companyResult = companyResultResultSet.getDouble("result");

                    Employee employee = new Employee();
                    employee.setType(Employee.Type.valueOf(employeeResultSet.getString("type")));
                    employee.setBase(employeeResultSet.getDouble("base_salary"));
                    employee.setAchievementsFactor(employeeResultSet.getDouble("achievements_factor"));
                    employee.setAchievements(employeeResultSet.getDouble("achievements"));
                    switch (employee.getType()) {
                        case SALES:
                            return employee.getBase() + employee.getAchievementsFactor() * employee.getAchievements()
                                    + companyResult * 0.0000001;
                        case HR:
                            return employee.getBase() + companyResult * 0.0000002;
                        case WORKER:
                            return employee.getBase();
                        case CEO:
                            return employee.getBase() + employee.getAchievements() * employee.getAchievementsFactor()
                                    + companyResult * 0.01;
                        default:
                            throw new IllegalStateException("Employee type unspecified");
                    }
                } finally {
                    if (companyResultResultSet != null) {
                        companyResultResultSet.close();
                    }
                    if (employeeResultSet != null) {
                        employeeResultSet.close();
                    }
                }
            } finally {
                if (getCompanyResult != null) {
                    getCompanyResult.close();
                }
                if (getEmployee != null) {
                    getEmployee.close();
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
