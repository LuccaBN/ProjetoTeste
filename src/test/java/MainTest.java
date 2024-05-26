import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

    public class MainTest {

        private static Connection con;

        @BeforeAll
        public static void setUp() {
            String driver = "org.h2.Driver";
            String user = "sa";
            String senha = "";
            String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

            try {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, senha);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Falha ao configurar banco de dados de teste");
            }
        }

        @AfterAll
        public static void tearDown() {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testMain() {
            assertNotNull(con, "A conexão com o banco de dados não deve ser nula");
            EstoqueController produto = new EstoqueController(con);
            assertNotNull(produto, "O objeto EstoqueController não deve ser nulo");
        }
    }