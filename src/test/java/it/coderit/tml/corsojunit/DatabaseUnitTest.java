package it.coderit.tml.corsojunit;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseUnitTest {

    @Autowired
    DataSource dataSource;
    Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = dataSource.getConnection();
        System.out.println("Connection OK");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE persone(id bigint primary key , nome varchar(255) not null)");
        statement.close();
        statement = connection.createStatement();
        statement.execute("INSERT INTO persone(id, nome) VALUES (1, 'Mario')");
        statement.close();
        statement = connection.createStatement();
        statement.execute("INSERT INTO persone(id, nome) VALUES (2, 'Marco')");
        statement.close();
        statement = connection.createStatement();
        statement.execute("CREATE PROCEDURE insert_data(id bigint, nome varchar(255))\n" +
                          " MODIFIES SQL DATA\n" +
                          "BEGIN\n" +
                          " ATOMIC \n" +
                          "    INSERT INTO persone(id, nome) VALUES (id, nome);\n" +
                          "END;\n");
        statement.close();

    }

    @AfterEach
    public void tearDown() throws Exception {
        Statement statement = connection.createStatement();
        statement.execute("DROP PROCEDURE insert_data");
        statement.close();
        statement = connection.createStatement();
        statement.execute("DROP TABLE persone");
        statement.close();
        connection.close();
    }

    @Test
    public void testConnection() throws Exception {
        Statement statement = connection.createStatement();
        boolean executed = statement.execute("select * from persone order by id asc");
        Assertions.assertTrue(executed);
        ResultSet resultSet = statement.getResultSet();
        Assertions.assertTrue(resultSet.next());
        long dual = resultSet.getLong(1);
        Assertions.assertEquals(1, dual);
    }

    @Test
    public void testConnection2() throws Exception {
        Statement statement = connection.createStatement();
        boolean executed = statement.execute("select * from persone order by id asc");
        Assertions.assertTrue(executed);
        ResultSet resultSet = statement.getResultSet();
        Assertions.assertTrue(resultSet.next());
        long dual = resultSet.getLong(1);
        Assertions.assertEquals(1, dual);
    }

    @Test
    public void testConnection3() throws Exception {

        try (CallableStatement statement = connection.prepareCall("{CALL insert_data(?, ?)}")) {
            statement.setInt(1, 42);
            statement.setString(2, "Utente 42");
            statement.execute();
        }

        try (Statement statement2 = connection.createStatement()) {
            boolean executed = statement2.execute("select * from persone order by id desc");
            Assertions.assertTrue(executed);
            try (ResultSet resultSet = statement2.getResultSet()) {
                Assertions.assertTrue(resultSet.next());
                long id = resultSet.getLong(1);
                Assertions.assertEquals(42, id);
            }
        }
    }

    @Test
    public void testConnection4() throws Exception {

        try (PreparedStatement ps = connection.prepareStatement("select * from persone where id = ?")) {
            ps.setLong(1, 1);
            boolean executed = ps.execute();
            Assertions.assertTrue(executed);

            try (ResultSet resultSet = ps.getResultSet()) {
                Assertions.assertTrue(resultSet.next());
                long id = resultSet.getLong(1);
                Assertions.assertEquals(1, id);
            }
        }
    }
}
