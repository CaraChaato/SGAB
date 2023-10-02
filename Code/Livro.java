import java.sql.*;
import java.util.Scanner;

public class Livro {

    private int idLivro;
    private String titulo;
    private String genero;
    private String autor;
    private Date dataPublicacao;
    private String edicao;
    private String editora;
    private String isbn;
    private boolean livroAcervo;
    private boolean livroDisponivel;

    public Livro(int idLivro, String titulo, String genero, String autor, Date dataPublicacao, String edicao,
            String editora, String isbn, boolean livroAcervo, boolean livroDisponivel) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.genero = genero;
        this.autor = autor;
        this.dataPublicacao = dataPublicacao;
        this.edicao = edicao;
        this.editora = editora;
        this.isbn = isbn;
        this.livroAcervo = livroAcervo;
        this.livroDisponivel = livroDisponivel;
    }

    /**
     * Metodo de busca por id
     * @param id
     * @return Livro
     */
    public static Livro BuscaLivroId(int id) { //Usado pelos administradores, para alteração, listagem e remoção
        try (Connection connection = PostgreSQLConnection.getInstance().getConnection()) {//se a conexão funcionar
            String query = "Select * from livro where idLivro = ?"; //cria a query, que é a pesquisa que iremos fazer
            PreparedStatement state = connection.prepareStatement(query); //cria o state, aquele que executa a pesquisa
            state.setInt(1, id); //preenche os ? com as informações desejadas
            ResultSet result = state.executeQuery(); //recebe a tabela com as respostas da pesquisa
            while (result.next()) {// enquanto houverem respostas, imprima-as
                // Corrigir a questão da Data
                return new Livro(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getDate(5), result.getString(6), result.getString(7), result.getString(8), result.getBoolean(9), result.getBoolean(10));
            }
        } catch (Exception e) {//se der erro, mostre qual foi
            System.out.println(e);
        }
        return null;
    }


    public static Livro BuscaLivro(Scanner sc) {
        String tipo, busca;
        System.out.println("Por que parametro deseja buscar o livro:\n1-Titulo;\n2-Autor;\n3-Genero;\n0-Voltar;");
        int op = sc.nextInt();
        sc.nextLine();
        switch (op) {//escolhendo o tipo de busca, que será usada na pesquisa posteriormente
            case 1:
                tipo = "titulo";
                System.out.print("Titulo: ");
                busca = sc.nextLine();//recebendo o que queremos buscar
                break;
            case 2:
                tipo = "autor";
                System.out.print("Autor: ");
                busca = sc.nextLine();
                break;
            case 3:
                tipo = "genero";
                System.out.print("Genero: ");
                busca = sc.nextLine();
                break;
            case 0: // sair
            default:
                return null;
        }

        try (Connection connection = PostgreSQLConnection.getInstance().getConnection()) {

            String query = "Select * from livro where "+ tipo +" like ? "; // Busca no banco de dados, neste caso, já que o ? é substituido por
            PreparedStatement state = connection.prepareStatement(query); // 'algo', usamos a varivavel diretamente para a pesquisa ficar correta
            state.setString(1, "%" + busca + "%");
            ResultSet result = state.executeQuery();// Resultados da execução da query.
            
            // Enquanto houverem linhas de resultados da busca para serem impressas, retorna-os.
            while (result.next()) {  
                return new Livro(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getDate(5), result.getString(6), result.getString(7), result.getString(8), result.getBoolean(9), result.getBoolean(10));    
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void inserirLivro() {
        try (Connection connection = PostgreSQLConnection.getInstance().getConnection()) {
            String query = "INSERT INTO Livro (titulo, genero, autor, dataPublicacao, edicao, editora, isbn, livroAcervo, livroDisponivel) VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, titulo);
            state.setString(2, genero);
            state.setString(3, autor);
            state.setDate(4, dataPublicacao);
            state.setString(5, edicao);
            state.setString(6, editora);
            state.setString(7, isbn);
            state.setBoolean(8, livroAcervo);
            state.setBoolean(9, livroDisponivel);
            state.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void excluirLivro() {

    }

    @Override
    public String toString() {
        return "Livro [idLivro=" + idLivro + ", titulo=" + titulo + ", genero=" + genero + ", autor=" + autor
                + ", dataPublicacao=" + dataPublicacao + ", edicao=" + edicao + ", editora=" + editora + ", isbn="
                + isbn + ", livroAcervo=" + livroAcervo + ", livroDisponivel=" + livroDisponivel;
    }

}
