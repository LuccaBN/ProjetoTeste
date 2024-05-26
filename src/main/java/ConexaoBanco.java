import java.sql.*;

public class ConexaoBanco {
    private Connection con = null;
    private Statement stmt;
    private ResultSet rs;
    private String url;
    private String usuario;
    private String senha;

    public void conectar(String strEnd, String strUsuario, String strSenha) {
        url = strEnd;
        usuario = strUsuario;
        senha = strSenha;
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection(url, usuario, strSenha);
            stmt = con.createStatement();
            System.out.println("Banco conectado com sucesso");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Erro ao conectar o driver");
            cnfe.printStackTrace();
        } catch (SQLException sqlex) {
            System.out.println("Erro na query");
            sqlex.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao desconectar o banco");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }
}
