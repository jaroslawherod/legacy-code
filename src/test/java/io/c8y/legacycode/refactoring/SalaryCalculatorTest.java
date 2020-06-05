package io.c8y.legacycode.refactoring;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SalaryCalculatorTest {

    private SalaryCalculator salaryCalculator;
    private JdbcConnectionPool datasource;

    @BeforeEach
    void setUp() throws NamingException, SQLException {
        final InitialContext initialContext = mock(InitialContext.class);
        final Context context = mock(Context.class);
        when(initialContext.lookup("java:/comp/env")).thenReturn(context);
        datasource = JdbcConnectionPool.create("jdbc:h2:mem:testdb", "sa", "sa");
        initializeDatabase();
        when(context.lookup("jdbc/datasource")).thenReturn(datasource);
        salaryCalculator = new SalaryCalculator(initialContext);

    }



    @Test
    public void shouldCreateInstanceOfSalaryCalculator() throws NamingException {
        new SalaryCalculator();
    }



    private void initializeDatabase() {
        withConnection((connection) -> {
            connection.createStatement().execute(
                    "CREATE TABLE EMPLOYEE (" +
                            " id bigint auto_increment," +
                            " type VARCHAR(100) ," +
                            " base_salary  decimal(30,10)," +
                            " achievements  decimal(30,10)," +
                            " achievements_factor  decimal(30,10)" +
                            ");"
            );
            connection.createStatement().execute(
                    "CREATE TABLE COMPANY_RESULT (" +
                            " month bigint ," +
                            " year bigint ," +
                            " result  decimal(30,10)" +
                            ");"
            );
        });
    }

    private void withConnection(ConnectionConsumer connectionConsumer) {
        try (final Connection connection = datasource.getConnection()) {
            connectionConsumer.execute(connection);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void insertUser(Employee.Type type, double salary, double achivements, double achivements_factor) {
        withConnection((conn) -> {
            final PreparedStatement statement = conn.prepareStatement(
                    "insert into employee (type,base_salary,achievements,achievements_factor) values(?,?,?,?)");
            statement.setString(1, type.name());
            statement.setDouble(2, salary);
            statement.setDouble(3, achivements);
            statement.setDouble(4, achivements_factor);

            statement.executeUpdate();
            conn.commit();
        });
    }

    private void insertCompanyResult(double result, int year, int month) {
        withConnection((conn) -> {
            final PreparedStatement statement = conn.prepareStatement(
                    "insert into COMPANY_RESULT (result,year,month) values(?,?,?)");
            statement.setDouble(1, result);
            statement.setInt(2, year);
            statement.setInt(3, month);

            statement.executeUpdate();
            conn.commit();
        });
    }

    @AfterEach
    public void cleanUp() {
        datasource.dispose();
    }



    interface ConnectionConsumer {
        void execute(Connection connection) throws Exception;
    }
}
