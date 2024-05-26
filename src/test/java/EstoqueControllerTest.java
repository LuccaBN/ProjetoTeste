import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class EstoqueControllerTest {

    private static Connection con;
    private EstoqueController controller;

    @BeforeAll
    public static void setUpBeforeClass() {
        String driver = "org.h2.Driver";
        String user = "sa";
        String senha = "";
        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            Statement stmt = con.createStatement();
            stmt.execute("CREATE TABLE fornecedor (id IDENTITY PRIMARY KEY, nome VARCHAR(255), contato VARCHAR(255))");
            stmt.execute("CREATE TABLE produto (id IDENTITY PRIMARY KEY, nome VARCHAR(255), quantidade INT)");
            stmt.execute("CREATE TABLE produto_fornecedor (id_produto INT, id_fornecedor INT, FOREIGN KEY (id_produto) REFERENCES produto(id), FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id))");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Falha ao configurar banco de dados de teste");
        }
    }

    @BeforeEach
    public void setUp() {
        controller = new EstoqueController(con);
    }

    @AfterEach
    public void tearDown() {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("DELETE FROM produto_fornecedor");
            stmt.execute("DELETE FROM produto");
            stmt.execute("DELETE FROM fornecedor");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDownAfterClass() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testCadastrarProduto() {
//        // Simule a entrada do usuário usando System.setIn() e System.setOut() se necessário
//        controller.cadastrarProduto();
//    }

    @Test
    public void testConsultarProdutosFornecedores() {
        controller.consultarProdutosFornecedores();
    }

//    @Test
//    public void testAtualizarProduto() {
//        controller.atualizarProduto();
//    }

//    @Test
//    public void testDeletarProduto() {
//        controller.deletarProduto();
//    }
}
