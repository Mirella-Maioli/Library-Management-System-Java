// Importa a classe LinkedList, que sera usada para criar a fila de espera.
import java.util.LinkedList;
// Importa a classe Objects, que ajuda a comparar e gerar codigos unicos dos objetos.
import java.util.Objects;
// Importa a interface Queue, que representa uma fila.
import java.util.Queue;

// Esta classe representa um livro da biblioteca.
// Pense nela como uma "ficha" que guarda todas as informacoes importantes sobre um livro.
public class Livro {
    // Guarda o nome do livro.
    private String titulo;
    // Guarda o nome de quem escreveu o livro.
    private String autor;
    // Guarda o ano em que o livro foi publicado.
    private int anoPublicacao;
    // Diz se o livro esta emprestado no momento.
    // true = esta emprestado
    // false = esta disponivel
    private boolean emprestado;
    // Guarda os nomes das pessoas que estao esperando pelo livro.
    // A primeira pessoa que entra na fila sera a primeira a ser atendida.
    private Queue<String> listaEspera;

    // Este e o construtor da classe.
    // Ele e chamado quando criamos um novo livro.
    // Aqui definimos os dados iniciais do livro.
    public Livro(String titulo, String autor, int anoPublicacao) {
        // Salva o titulo recebido dentro do atributo titulo.
        this.titulo = titulo;
        // Salva o autor recebido dentro do atributo autor.
        this.autor = autor;
        // Salva o ano recebido dentro do atributo anoPublicacao.
        this.anoPublicacao = anoPublicacao;
        this.emprestado = false;
        // Cria uma fila vazia para guardar os usuarios que esperarem por este livro.
        this.listaEspera = new LinkedList<>();
    }

    // Este metodo devolve o titulo do livro.
    public String getTitulo() {
        // Retorna o valor guardado em titulo.
        return titulo;
    }

    // Este metodo devolve o autor do livro.
    public String getAutor() {
        // Retorna o valor guardado em autor.
        return autor;
    }

    // Este metodo devolve o ano de publicacao do livro.
    public int getAnoPublicacao() {
        // Retorna o valor guardado em anoPublicacao.
        return anoPublicacao;
    }

    // Este metodo informa se o livro esta emprestado.
    public boolean isEmprestado() {
        // Retorna true ou false.
        return emprestado;
    }

    // Este metodo marca o livro como emprestado.
    public void emprestar() {
        // Muda o valor para true.
        emprestado = true;
    }

    // Este metodo marca o livro como devolvido.
    public void devolver() {
        // Muda o valor para false, deixando o livro disponivel novamente.
        emprestado = false;
    }

    // Este metodo coloca uma pessoa no fim da fila de espera.
    public void adicionarNaListaEspera(String usuario) {
        // Adiciona o nome recebido ao final da fila.
        listaEspera.offer(usuario);
    }

    // Este metodo chama a proxima pessoa da fila.
    public String chamarProximoDaFila() {
        // Remove e devolve a primeira pessoa da fila.
        return listaEspera.poll();
    }

    // Este metodo verifica se existe alguem esperando por este livro.
    public boolean possuiListaEspera() {
        // Retorna true se a fila nao estiver vazia.
        return !listaEspera.isEmpty();
    }

    // Este metodo devolve uma copia da fila.
    // Isso protege a fila original contra alteracoes feitas fora da classe.
    public Queue<String> getListaEspera() {
        // Cria e devolve uma nova fila com os mesmos nomes da fila original.
        return new LinkedList<>(listaEspera);
    }

    // Este metodo compara dois objetos Livro para saber se representam o mesmo livro.
    @Override
    public boolean equals(Object obj) {
        // Se os dois apontarem exatamente para o mesmo objeto na memoria,
        // eles sao iguais.
        if (this == obj) {
            return true;
        }

        // Se o objeto recebido nao for um Livro, nao pode ser igual.
        if (!(obj instanceof Livro)) {
            return false;
        }

        // Converte o objeto genérico para o tipo Livro.
        Livro outroLivro = (Livro) obj;
        // Compara ano, titulo e autor.
        // equalsIgnoreCase ignora diferencas entre letras maiusculas e minusculas.
        return anoPublicacao == outroLivro.anoPublicacao
                && titulo.equalsIgnoreCase(outroLivro.titulo)
                && autor.equalsIgnoreCase(outroLivro.autor);
    }

    // Este metodo gera um codigo numerico para o objeto.
    // Ele e importante quando usamos Livro em estruturas como HashMap e HashSet.
    @Override
    public int hashCode() {
        // Gera o codigo usando titulo, autor e ano.
        // toLowerCase ajuda a manter o mesmo codigo mesmo que a pessoa escreva com letras diferentes.
        return Objects.hash(titulo.toLowerCase(), autor.toLowerCase(), anoPublicacao);
    }

    // Este metodo monta uma frase bonita para mostrar o livro na tela.
    @Override
    public String toString() {
        // Junta varias informacoes em um unico texto.
        return "Titulo: " + titulo
                + " | Autor: " + autor
                + " | Ano de publicacao: " + anoPublicacao
                + " | Status: " + (emprestado ? "Emprestado" : "Disponivel");
    }
}
