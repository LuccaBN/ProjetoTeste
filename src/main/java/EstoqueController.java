import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EstoqueController {

    static Scanner scanner = new Scanner(System.in);
    private Connection conexao;

    public EstoqueController(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrarProduto() {
        try {
            System.out.println("Digite o nome do produto:");
            String nomeProduto = scanner.nextLine();

            System.out.println("Digite a quantidade do produto:");
            int quantidadeProduto = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o nome do fornecedor:");
            String nomeFornecedor = scanner.nextLine();

            System.out.println("Digite o contato do fornecedor:");
            String contatoFornecedor = scanner.nextLine();

            String sqlFornecedor = "INSERT INTO fornecedor (nome, contato) VALUES (?, ?)";
            int idFornecedor = -1;
            try (PreparedStatement statementFornecedor = conexao.prepareStatement(sqlFornecedor, Statement.RETURN_GENERATED_KEYS)) {
                statementFornecedor.setString(1, nomeFornecedor);
                statementFornecedor.setString(2, contatoFornecedor);

                int rowsInsertedFornecedor = statementFornecedor.executeUpdate();
                if (rowsInsertedFornecedor > 0) {
                    System.out.println("Fornecedor cadastrado com sucesso.");
                    ResultSet generatedKeys = statementFornecedor.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        idFornecedor = generatedKeys.getInt(1);
                    }
                } else {
                    System.out.println("Falha ao cadastrar o fornecedor.");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sqlProduto = "INSERT INTO produto (nome, quantidade) VALUES (?, ?)";
            int idProduto = -1;
            try (PreparedStatement statementProduto = conexao.prepareStatement(sqlProduto, Statement.RETURN_GENERATED_KEYS)) {
                statementProduto.setString(1, nomeProduto);
                statementProduto.setInt(2, quantidadeProduto);

                int rowsInsertedProduto = statementProduto.executeUpdate();
                if (rowsInsertedProduto > 0) {
                    System.out.println("Produto cadastrado com sucesso.");
                    ResultSet generatedKeys = statementProduto.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        idProduto = generatedKeys.getInt(1);
                    }
                } else {
                    System.out.println("Falha ao cadastrar o produto.");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sqlProdutoFornecedor = "INSERT INTO produto_fornecedor (id_produto, id_fornecedor) VALUES (?, ?)";
            try (PreparedStatement statementProdutoFornecedor = conexao.prepareStatement(sqlProdutoFornecedor)) {
                statementProdutoFornecedor.setInt(1, idProduto);
                statementProdutoFornecedor.setInt(2, idFornecedor);

                int rowsInsertedProdutoFornecedor = statementProdutoFornecedor.executeUpdate();
                if (rowsInsertedProdutoFornecedor > 0) {
                    System.out.println("Produto associado ao fornecedor com sucesso.");
                } else {
                    System.out.println("Falha ao associar o produto ao fornecedor.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir valores corretos.");
            scanner.nextLine();
        }
    }

    public void consultarProdutosFornecedores() {
        try {
            String sql = "SELECT fornecedor.id,fornecedor.nome, fornecedor.contato, " +
                    "produto.nome, produto.quantidade " +
                    "FROM fornecedor "+
                    "INNER JOIN produto ON fornecedor.id = produto.id";

            PreparedStatement statement = conexao.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Produtos e seus fornecedores:");
            System.out.println("------------------------------");

            while (resultSet.next()) {
                int idFornecedor = resultSet.getInt("fornecedor.id");
                String nomeFornecedor = resultSet.getString("fornecedor.nome");
                String contatoFornecedor = resultSet.getString("fornecedor.contato");
                String nomeProduto = resultSet.getString("produto.nome");
                int quantidadeProduto = resultSet.getInt("produto.quantidade");


                System.out.println("ID Fornecedor: " + idFornecedor);
                System.out.println("Fornecedor: " + nomeFornecedor + " (Contato: " + contatoFornecedor + ")");
                System.out.println("Nome Produto: " + nomeProduto + " (Quantidade: " + quantidadeProduto + ")");

                System.out.println("------------------------------");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarProduto() {
        consultarProdutosFornecedores();
        try {
            System.out.println("Digite o ID do produto que deseja atualizar:");
            int idProdutoAtualizar = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o novo nome do produto:");
            String novoNomeProduto = scanner.nextLine();

            System.out.println("Digite a nova quantidade do produto:");
            int novaQuantidadeProduto = scanner.nextInt();
            scanner.nextLine();

            String sqlUpdateProduto = "UPDATE produto SET nome = ?, quantidade = ? WHERE id = ?";
            try (PreparedStatement statementUpdateProduto = conexao.prepareStatement(sqlUpdateProduto)) {
                statementUpdateProduto.setString(1, novoNomeProduto);
                statementUpdateProduto.setInt(2, novaQuantidadeProduto);
                statementUpdateProduto.setInt(3, idProdutoAtualizar);

                int rowsUpdated = statementUpdateProduto.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Produto atualizado com sucesso.");
                } else {
                    System.out.println("Falha ao atualizar o produto. Verifique o ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir um valor numérico para o ID do produto ou valores corretos para as informações a serem atualizadas.");
            scanner.nextLine();
        }
    }

    public void deletarProduto() {
        consultarProdutosFornecedores();
        try {
            System.out.println("Digite o ID do produto que deseja deletar:");
            int idProdutoDeletar = scanner.nextInt();
            scanner.nextLine();

            int idFornecedor = -1;
            String sqlSelectFornecedor = "SELECT id_fornecedor FROM produto_fornecedor WHERE id_produto = ?";
            try (PreparedStatement statementSelectFornecedor = conexao.prepareStatement(sqlSelectFornecedor)) {
                statementSelectFornecedor.setInt(1, idProdutoDeletar);
                ResultSet resultSet = statementSelectFornecedor.executeQuery();
                if (resultSet.next()) {
                    idFornecedor = resultSet.getInt("id_fornecedor");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sqlDeleteFornecedorProduto = "DELETE FROM produto_fornecedor WHERE id_produto = ?";
            try (PreparedStatement statementDeleteFornecedorProduto = conexao.prepareStatement(sqlDeleteFornecedorProduto)) {
                statementDeleteFornecedorProduto.setInt(1, idProdutoDeletar);
                statementDeleteFornecedorProduto.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sqlDeleteProduto = "DELETE FROM produto WHERE id = ?";
            try (PreparedStatement statementDeleteProduto = conexao.prepareStatement(sqlDeleteProduto)) {
                statementDeleteProduto.setInt(1, idProdutoDeletar);
                int rowsDeleted = statementDeleteProduto.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Produto deletado com sucesso.");

                    if (idFornecedor != -1) {
                        String sqlDeleteFornecedor = "DELETE FROM fornecedor WHERE id = ?";
                        try (PreparedStatement statementDeleteFornecedor = conexao.prepareStatement(sqlDeleteFornecedor)) {
                            statementDeleteFornecedor.setInt(1, idFornecedor);
                            statementDeleteFornecedor.executeUpdate();
                            System.out.println("Fornecedor do produto também foi deletado.");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Falha ao deletar o produto. Verifique o ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir um valor numérico para o ID do produto.");
            scanner.nextLine();
        }
    }
}
