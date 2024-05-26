import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ConexaoBancoTest {

    private ConexaoBanco conexaoBanco;

    @BeforeEach
    public void setUp() {
        conexaoBanco = new ConexaoBanco();
        conexaoBanco.conectar("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @AfterEach
    public void tearDown() {
        conexaoBanco.desconectar();
    }

    @Test
    public void testConectar() {
        Connection con = conexaoBanco.getConnection();
        assertNotNull(con, "A conexão com o banco de dados não deve ser nula");

        try (Statement stmt = con.createStatement()) {
            assertTrue(stmt.execute("SELECT 1"), "A consulta de teste deve retornar resultados");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Erro ao executar consulta de teste");
        }
    }

    @Test
    public void testDesconectar() {
        conexaoBanco.desconectar();
        Connection con = conexaoBanco.getConnection();
        assertThrows(SQLException.class, con::isClosed, "A conexão deve estar fechada após desconectar");
    }
}
