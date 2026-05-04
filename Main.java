import java.util.List;
import java.util.Scanner;

// Esta e a classe principal do programa.
// E daqui que a execucao comeca.
public class Main {

    // O metodo main e a porta de entrada do programa.
    // Quando executamos o projeto, e este metodo que o Java chama primeiro.
    public static void main(String[] args) {
        // Cria um objeto da classe BibliotecaVirtual.
        // Esse objeto sera usado para controlar toda a biblioteca.
        BibliotecaVirtual biblioteca = new BibliotecaVirtual();

        // Adiciona os livros da trilogia Senhor dos Aneis.
        biblioteca.adicionarLivro("The Fellowship of the Ring", "J. R. R. Tolkien", 1954);
        biblioteca.adicionarLivro("The Two Towers", "J. R. R. Tolkien", 1954);
        biblioteca.adicionarLivro("The Return of the King", "J. R. R. Tolkien", 1955);

        // Adiciona os 5 volumes de "A Morte e o Unico Final para a Vila".
        biblioteca.adicionarLivro("A Morte e o Unico Final para a Vila - Volume 1", "Gwon Gyeoeul", 2020);
        biblioteca.adicionarLivro("A Morte e o Unico Final para a Vila - Volume 2", "Gwon Gyeoeul", 2020);
        biblioteca.adicionarLivro("A Morte e o Unico Final para a Vila - Volume 3", "Gwon Gyeoeul", 2021);
        biblioteca.adicionarLivro("A Morte e o Unico Final para a Vila - Volume 4", "Gwon Gyeoeul", 2021);
        biblioteca.adicionarLivro("A Morte e o Unico Final para a Vila - Volume 5", "Gwon Gyeoeul", 2022);

        // Adiciona "O Principe".
        biblioteca.adicionarLivro("O Principe", "Nicolau Maquiavel", 1532);

        // Adiciona "O Hobbit" para completar os 10 livros.
        biblioteca.adicionarLivro("O Hobbit", "J. R. R. Tolkien", 1937);

        // Chama o metodo que vai ligar os livros no grafo de recomendacoes.
        conectarRecomendacoes(biblioteca);

        // Pede ao usuario uma pesquisa para encontrar livros na biblioteca.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome, autor ou ano de um livro para buscar:");
        String pesquisa = scanner.nextLine();

        List<Livro> resultadosPesquisa = biblioteca.pesquisarLivros(pesquisa);
        Livro livroSelecionado = escolherLivro(scanner, resultadosPesquisa);

        if (livroSelecionado == null) {
            System.out.println("Nenhum livro foi selecionado.");
            System.out.println();
            biblioteca.listarLivros();
            scanner.close();
            return;
        }

        exibirMenuDeResultados(scanner, biblioteca, livroSelecionado);
        scanner.close();
    }

    // Este metodo auxiliar cria todas as ligacoes do grafo.
    // Ele recebe a biblioteca pronta e conecta os livros relacionados.
    private static void conectarRecomendacoes(BibliotecaVirtual biblioteca) {
        // Liga os livros da trilogia entre si e com O Hobbit.
        biblioteca.conectarLivros("The Fellowship of the Ring", "The Two Towers");
        biblioteca.conectarLivros("The Fellowship of the Ring", "The Return of the King");
        biblioteca.conectarLivros("The Fellowship of the Ring", "O Hobbit");

        biblioteca.conectarLivros("The Two Towers", "The Return of the King");
        biblioteca.conectarLivros("The Two Towers", "O Hobbit");

        biblioteca.conectarLivros("The Return of the King", "O Hobbit");

        // Liga os volumes da colecao "A Morte e o Unico Final para a Vila".
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 1",
                "A Morte e o Unico Final para a Vila - Volume 2");
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 1",
                "A Morte e o Unico Final para a Vila - Volume 3");
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 1",
                "O Principe");

        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 2",
                "A Morte e o Unico Final para a Vila - Volume 3");
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 2",
                "A Morte e o Unico Final para a Vila - Volume 4");

        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 3",
                "A Morte e o Unico Final para a Vila - Volume 4");
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 3",
                "A Morte e o Unico Final para a Vila - Volume 5");

        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 4",
                "A Morte e o Unico Final para a Vila - Volume 5");
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 4",
                "O Principe");

        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 5",
                "O Principe");
        biblioteca.conectarLivros("A Morte e o Unico Final para a Vila - Volume 5",
                "O Hobbit");

        // Cria uma ultima ligacao entre O Principe e O Hobbit.
        biblioteca.conectarLivros("O Principe", "O Hobbit");
    }

    // Este metodo mostra os titulos percorridos pela busca.
    private static void exibirPercurso(List<String> percurso) {
        if (percurso.isEmpty()) {
            System.out.println("Nenhum livro foi percorrido.");
            return;
        }

        for (String titulo : percurso) {
            System.out.println("- " + titulo);
        }
    }

    // Este metodo cria um cabecalho para organizar melhor a saida no terminal.
    private static void exibirSecao(String titulo) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(titulo.toUpperCase());
        System.out.println("==================================================");
    }

    // Este metodo mostra um menu para o usuario escolher quais dados quer ver.
    private static void exibirMenuDeResultados(Scanner scanner, BibliotecaVirtual biblioteca, Livro livroSelecionado) {
        String tituloBuscado = livroSelecionado.getTitulo();
        boolean historicoRegistrado = false;
        boolean emprestimoSimulado = false;
        int opcao;

        do {
            exibirMenu();
            opcao = lerOpcaoMenu(scanner);

            switch (opcao) {
                case 1:
                    exibirDadosDoLivro(livroSelecionado);
                    break;
                case 2:
                    exibirBuscaNaArvore(biblioteca, tituloBuscado);
                    break;
                case 3:
                    exibirRecomendacoesDoLivro(biblioteca, tituloBuscado);
                    break;
                case 4:
                    historicoRegistrado = exibirHistorico(biblioteca, tituloBuscado, historicoRegistrado);
                    break;
                case 5:
                    exibirRecomendacoesPorHistorico(biblioteca);
                    break;
                case 6:
                    emprestimoSimulado = exibirEmprestimoEFila(biblioteca, tituloBuscado, emprestimoSimulado);
                    break;
                case 7:
                    exibirGrafo(biblioteca);
                    break;
                case 8:
                    exibirDijkstra(biblioteca, tituloBuscado);
                    break;
                case 9:
                    exibirEstadoFinal(biblioteca);
                    break;
                case 10:
                    exibirDadosDoLivro(livroSelecionado);
                    exibirBuscaNaArvore(biblioteca, tituloBuscado);
                    exibirRecomendacoesDoLivro(biblioteca, tituloBuscado);
                    historicoRegistrado = exibirHistorico(biblioteca, tituloBuscado, historicoRegistrado);
                    exibirRecomendacoesPorHistorico(biblioteca);
                    emprestimoSimulado = exibirEmprestimoEFila(biblioteca, tituloBuscado, emprestimoSimulado);
                    exibirGrafo(biblioteca);
                    exibirDijkstra(biblioteca, tituloBuscado);
                    exibirEstadoFinal(biblioteca);
                    break;
                case 0:
                    System.out.println("Consulta finalizada.");
                    break;
                default:
                    System.out.println("Opcao invalida. Escolha um numero do menu.");
                    break;
            }
        } while (opcao != 0);
    }

    // Este metodo imprime as opcoes disponiveis depois da escolha do livro.
    private static void exibirMenu() {
        System.out.println();
        System.out.println("+------------------------------------------------+");
        System.out.println("|             PAINEL DA BIBLIOTECA              |");
        System.out.println("+------------------------------------------------+");
        System.out.println("|  1 - Dados do livro selecionado                |");
        System.out.println("|  2 - Busca na arvore (DFS e BFS)               |");
        System.out.println("|  3 - Recomendacoes diretas do livro            |");
        System.out.println("|  4 - Historico de navegacao                    |");
        System.out.println("|  5 - Recomendacoes pelo historico              |");
        System.out.println("|  6 - Emprestimo e fila de espera               |");
        System.out.println("|  7 - Grafo de recomendacoes                    |");
        System.out.println("|  8 - Dijkstra: livros mais proximos            |");
        System.out.println("|  9 - Estado final da biblioteca                |");
        System.out.println("| 10 - Mostrar tudo                              |");
        System.out.println("|  0 - Sair                                      |");
        System.out.println("+------------------------------------------------+");
        System.out.print("Digite a opcao desejada: ");
    }

    // Este metodo le uma opcao do menu principal de resultados.
    private static int lerOpcaoMenu(Scanner scanner) {
        String entrada = scanner.nextLine().trim();

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException erro) {
            return -1;
        }
    }

    // Este metodo exibe apenas os dados do livro selecionado.
    private static void exibirDadosDoLivro(Livro livroSelecionado) {
        exibirSecao("Livro selecionado");
        System.out.println(livroSelecionado);
    }

    // Este metodo exibe a busca na arvore pelos dois percursos.
    private static void exibirBuscaNaArvore(BibliotecaVirtual biblioteca, String tituloBuscado) {
        Livro livroEncontrado = biblioteca.buscarLivroNaArvore(tituloBuscado);
        List<String> percursoDFS = biblioteca.getPercursoDFS(tituloBuscado);
        List<String> percursoBFS = biblioteca.getPercursoBFS(tituloBuscado);

        exibirSecao("Busca na arvore");
        System.out.println("Percurso DFS ate o resultado:");
        exibirPercurso(percursoDFS);
        System.out.println();
        System.out.println("Percurso BFS ate o resultado:");
        exibirPercurso(percursoBFS);
        System.out.println();

        if (livroEncontrado != null) {
            System.out.println("Resultado: livro encontrado na arvore.");
        } else {
            System.out.println("Resultado: livro nao encontrado na arvore.");
        }
    }

    // Este metodo exibe as recomendacoes diretas do livro escolhido.
    private static void exibirRecomendacoesDoLivro(BibliotecaVirtual biblioteca, String tituloBuscado) {
        exibirSecao("Recomendacoes");
        biblioteca.recomendarPorLivro(tituloBuscado);
    }

    // Este metodo registra a visualizacao uma vez e mostra o historico.
    private static boolean exibirHistorico(BibliotecaVirtual biblioteca, String tituloBuscado, boolean historicoRegistrado) {
        exibirSecao("Historico de navegacao");

        if (!historicoRegistrado) {
            biblioteca.visualizarLivro(tituloBuscado);
            historicoRegistrado = true;
        }

        biblioteca.exibirHistoricoNavegacao();
        return historicoRegistrado;
    }

    // Este metodo mostra recomendacoes baseadas nos livros vistos.
    private static void exibirRecomendacoesPorHistorico(BibliotecaVirtual biblioteca) {
        exibirSecao("Recomendacoes pelo historico");
        biblioteca.recomendarPorHistorico();
    }

    // Este metodo simula o emprestimo uma vez e mostra a fila.
    private static boolean exibirEmprestimoEFila(BibliotecaVirtual biblioteca, String tituloBuscado, boolean emprestimoSimulado) {
        exibirSecao("Emprestimo e fila de espera");

        if (!emprestimoSimulado) {
            biblioteca.emprestarLivro(tituloBuscado, "Ana");
            biblioteca.emprestarLivro(tituloBuscado, "Bruno");
            biblioteca.emprestarLivro(tituloBuscado, "Carla");
            System.out.println();
            biblioteca.exibirListaEspera(tituloBuscado);
            System.out.println();
            biblioteca.devolverLivro(tituloBuscado);
            return true;
        }

        System.out.println("A simulacao de emprestimo ja foi executada para este livro.");
        biblioteca.exibirListaEspera(tituloBuscado);
        return emprestimoSimulado;
    }

    // Este metodo exibe o grafo completo de recomendacoes.
    private static void exibirGrafo(BibliotecaVirtual biblioteca) {
        exibirSecao("Grafo de recomendacoes");
        biblioteca.exibirGrafoRecomendacoes();
    }

    // Este metodo exibe as menores distancias entre o livro selecionado e os demais.
    private static void exibirDijkstra(BibliotecaVirtual biblioteca, String tituloBuscado) {
        exibirSecao("Dijkstra - livros mais proximos");
        biblioteca.recomendarPorDijkstra(tituloBuscado);
    }

    // Este metodo exibe o estado atual de todos os livros.
    private static void exibirEstadoFinal(BibliotecaVirtual biblioteca) {
        exibirSecao("Estado final da biblioteca");
        biblioteca.listarLivros();
    }

    // Este metodo mostra os resultados encontrados e permite escolher um deles.
    private static Livro escolherLivro(Scanner scanner, List<Livro> resultadosPesquisa) {
        if (resultadosPesquisa.isEmpty()) {
            System.out.println("Nenhum livro foi encontrado para essa pesquisa.");
            return null;
        }

        if (resultadosPesquisa.size() == 1) {
            Livro unicoResultado = resultadosPesquisa.get(0);
            System.out.println("Livro encontrado: " + unicoResultado);
            return unicoResultado;
        }

        System.out.println("Foram encontrados estes livros:");
        for (int i = 0; i < resultadosPesquisa.size(); i++) {
            System.out.println((i + 1) + " - " + resultadosPesquisa.get(i));
        }

        System.out.println("Digite o numero do livro que deseja selecionar:");
        int opcao = lerOpcao(scanner, resultadosPesquisa.size());

        if (opcao == -1) {
            System.out.println("Opcao invalida.");
            return null;
        }

        Livro livroSelecionado = resultadosPesquisa.get(opcao - 1);
        System.out.println("Livro selecionado: " + livroSelecionado);
        return livroSelecionado;
    }

    // Este metodo le a opcao escolhida e verifica se ela existe na lista.
    private static int lerOpcao(Scanner scanner, int quantidadeOpcoes) {
        if (!scanner.hasNextInt()) {
            return -1;
        }

        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > quantidadeOpcoes) {
            return -1;
        }

        return opcao;
    }
}
