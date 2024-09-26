package it.coderit.tml.corsojunit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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
        int modified = statement.executeUpdate("INSERT INTO persone(id, nome) VALUES (1, 'Mario')");
        statement.close();
        Assertions.assertEquals(1, modified);
        statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO persone(id, nome) VALUES (2, 'Marco')");
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
    public void testSimpleStatement() throws Exception {
        Statement statement = connection.createStatement();
        boolean executed = statement.execute("select id, nome from persone order by id asc");
        Assertions.assertTrue(executed);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            Assertions.assertTrue(id > 0);
            if (id == 1) {
                Assertions.assertEquals("Mario", resultSet.getString("nome"));
            } else {
                Assertions.assertEquals("Marco", resultSet.getString("nome"));
            }
        }
    }

    @Test
    public void testPrepareStatement() throws Exception {

        try (PreparedStatement ps = connection.prepareStatement("select id, nome from persone where id = ?")) {
            ps.setLong(1, 1);
            boolean executed = ps.execute();
            Assertions.assertTrue(executed);
            try (ResultSet resultSet = ps.getResultSet()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    Assertions.assertEquals(1, id);
                    Assertions.assertEquals("Mario", resultSet.getString("nome"));
                }
            }
        }
    }

    @Test
    public void testProcedure() throws Exception {

        try (CallableStatement statement = connection.prepareCall("{ CALL insert_data(?, ?) }")) {
            statement.setLong(1, 42);
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
    public void testProcedureFail() throws Exception {

        try (CallableStatement statement = connection.prepareCall("{ CALL insert_data(?, ?) }")) {
            statement.setLong(1, 1);
            statement.setString(2, "Utente 42");
            statement.execute();
        }
    }

    @Test
    public void testTx() throws Exception {
        connection.setAutoCommit(false);
        try (CallableStatement statement = connection.prepareCall("{ CALL insert_data(?, ?) }")) {
            statement.setLong(1, 1);
            statement.setString(2, "Utente 42");
            statement.execute();
        }
        connection.commit();
    }

    @Test
    public void testLoadFromCsv() throws Exception {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setRecordSeparator('\n')
                .setDelimiter(',')
                .setSkipHeaderRecord(true)
                .setHeader(new String[]{"id", "nome"})
                .build();

        connection.setAutoCommit(false);
        Reader in = new FileReader("src/test/resources/utenti.csv");
        Iterable<CSVRecord> records = csvFormat.parse(in);
        for (CSVRecord record : records) {
            String id = record.get("id");
            String nome = record.get("nome");
            try (CallableStatement statement = connection.prepareCall("{ CALL insert_data(?, ?) }")) {
                statement.setLong(1, Long.parseLong(id));
                statement.setString(2, nome);
                statement.execute();
            }

        }
        connection.commit();

        try (CSVPrinter printer = csvFormat.print(new File("src/test/resources/utenti-out.csv"), StandardCharsets.UTF_8)) {
            printer.printRecord("id", "nome");
            try (CallableStatement statement = connection.prepareCall("select id, nome from persone;")) {
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String nome = resultSet.getString("nome");
                    printer.printRecord(id, nome);
                }
            }
        }
    }
}
