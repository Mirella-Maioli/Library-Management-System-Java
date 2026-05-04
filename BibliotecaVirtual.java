// Importa o HashMap, que sera usado para montar o grafo de recomendacoes.
import java.util.HashMap;
// Importa o HashSet, que sera usado para evitar recomendacoes repetidas.
import java.util.HashSet;
// Importa o ArrayList para guardar o percurso das buscas na arvore.
import java.util.ArrayList;
// Importa a LinkedList, que sera usada para guardar os livros cadastrados.
import java.util.LinkedList;
// Importa a Queue para a busca em largura.
import java.util.Queue;
// Importa a List para devolver os percursos encontrados.
import java.util.List;
// Importa a interface Set, que representa um conjunto sem repeticoes.
import java.util.Set;
// Importa a Stack, que sera usada como historico de navegacao.
import java.util.Stack;
// Importa Map para guardar as distancias calculadas pelo algoritmo de Dijkstra.
import java.util.Map;
// Importa Collections para ordenar listas de resultados.
import java.util.Collections;
// Importa Comparator para definir a ordem de exibicao dos livros.
import java.util.Comparator;
// Importa o Normalizer, que ajuda a comparar textos ignorando acentos.
import java.text.Normalizer;

// Esta classe e o "cerebro" da biblioteca.
// Ela controla os livros, o historico, os emprestimos e as recomendacoes.
public class BibliotecaVirtual {
    // Guarda todos os livros cadastrados.
    private LinkedList<Livro> livros;
    // Este e o grafo de recomendacoes.
    // Cada livro aponta para um conjunto de livros parecidos ou relacionados.
    private HashMap<Livro, Set<Livro>> grafoRecomendacoes;
    // Guarda o historico de livros visualizados.
    // O ultimo livro visto fica por ultimo na pilha.
    private Stack<Livro> historicoNavegacao;
    // Guarda a raiz da arvore binaria usada nas buscas de livros.
    private No raizArvoreLivros;

    // Este e o construtor da biblioteca.
    // Aqui criamos as estruturas vazias para comecar a usar o sistema.
    public BibliotecaVirtual() {
        // Cria a lista onde os livros serao guardados.
        livros = new LinkedList<>();
        // Cria o mapa que vai guardar as ligacoes de recomendacao.
        grafoRecomendacoes = new HashMap<>();
        // Cria a pilha do historico.
        historicoNavegacao = new Stack<>();
        // Comeca com a arvore vazia.
        raizArvoreLivros = null;
    }

    // Este metodo cria e adiciona um novo livro ao sistema.
    public void adicionarLivro(String titulo, String autor, int anoPublicacao) {
        // Cria um objeto Livro com os dados recebidos.
        Livro novoLivro = new Livro(titulo, autor, anoPublicacao);
        // Coloca o livro na lista principal da biblioteca.
        livros.add(novoLivro);
        // Insere o livro tambem na arvore binaria por titulo.
        raizArvoreLivros = inserirNaArvore(raizArvoreLivros, novoLivro);
        // Garante que o livro tambem exista no grafo, mesmo sem recomendacoes ainda.
        grafoRecomendacoes.putIfAbsent(novoLivro, new HashSet<>());
    }

    // Este metodo faz a busca principal na arvore usando DFS.
    public Livro buscarLivroNaArvore(String titulo) {
        return buscarLivroNaArvoreDFS(titulo);
    }

    // Este metodo procura livros de forma flexivel.
    // Ele aceita partes do titulo, palavras soltas, nome do autor ou ano, ignorando acentos e maiusculas.
    public List<Livro> pesquisarLivros(String pesquisa) {
        List<Livro> resultados = new ArrayList<>();
        String pesquisaNormalizada = normalizarTexto(pesquisa);

        if (pesquisaNormalizada.isEmpty()) {
            return resultados;
        }

        for (Livro livro : livros) {
            String tituloNormalizado = normalizarTexto(livro.getTitulo());
            String autorNormalizado = normalizarTexto(livro.getAutor());
            String ano = String.valueOf(livro.getAnoPublicacao());

            if (tituloNormalizado.contains(pesquisaNormalizada)
                    || autorNormalizado.contains(pesquisaNormalizada)
                    || ano.contains(pesquisaNormalizada)
                    || textoContemTodasAsPalavras(tituloNormalizado, pesquisaNormalizada)
                    || textoContemTodasAsPalavras(autorNormalizado, pesquisaNormalizada)) {
                resultados.add(livro);
            }
        }

        return resultados;
    }

    // Este metodo percorre a arvore em profundidade ate encontrar o livro.
    public Livro buscarLivroNaArvoreDFS(String titulo) {
        return buscarLivroNaArvoreDFS(raizArvoreLivros, titulo);
    }

    // Este metodo percorre a arvore em largura ate encontrar o livro.
    public Livro buscarLivroNaArvoreBFS(String titulo) {
        if (raizArvoreLivros == null) {
            return null;
        }

        String tituloNormalizado = normalizarTexto(titulo);

        Queue<No> fila = new LinkedList<>();
        fila.add(raizArvoreLivros);

        while (!fila.isEmpty()) {
            No atual = fila.poll();
            if (normalizarTexto(atual.livro.getTitulo()).equals(tituloNormalizado)) {
                return atual.livro;
            }
            if (atual.esquerda != null) {
                fila.add(atual.esquerda);
            }
            if (atual.direita != null) {
                fila.add(atual.direita);
            }
        }

        return null;
    }

    // Este metodo devolve o percurso visitado pela DFS ate encontrar o titulo informado.
    public List<String> getPercursoDFS(String titulo) {
        List<String> percurso = new ArrayList<>();
        percorrerDFS(raizArvoreLivros, titulo, percurso);
        return percurso;
    }

    // Este metodo devolve o percurso visitado pela BFS ate encontrar o titulo informado.
    public List<String> getPercursoBFS(String titulo) {
        List<String> percurso = new ArrayList<>();

        if (raizArvoreLivros == null) {
            return percurso;
        }

        String tituloNormalizado = normalizarTexto(titulo);

        Queue<No> fila = new LinkedList<>();
        fila.add(raizArvoreLivros);

        while (!fila.isEmpty()) {
            No atual = fila.poll();
            percurso.add(atual.livro.getTitulo());

            if (normalizarTexto(atual.livro.getTitulo()).equals(tituloNormalizado)) {
                break;
            }
            if (atual.esquerda != null) {
                fila.add(atual.esquerda);
            }
            if (atual.direita != null) {
                fila.add(atual.direita);
            }
        }

        return percurso;
    }

    // Este metodo mostra todos os livros cadastrados.
    public void listarLivros() {
        // Se a lista estiver vazia, mostra um aviso e termina o metodo.
        if (livros.isEmpty()) {
            System.out.println("A biblioteca esta vazia.");
            return;
        }

        // Mostra um titulo para a lista.
        System.out.println("Livros cadastrados na biblioteca:");
        // Passa por cada livro da lista.
        for (Livro livro : livros) {
            // Mostra o livro atual.
            System.out.println(livro);
        }
    }

    // Este metodo simula quando uma pessoa abre ou consulta um livro.
    public void visualizarLivro(String titulo) {
        // Procura o livro pelo titulo.
        Livro livro = buscarLivroPorTitulo(titulo);
        // Se nao encontrar, avisa e encerra o metodo.
        if (livro == null) {
            System.out.println("Livro nao encontrado.");
            return;
        }

        // Coloca o livro no historico de navegacao.
        historicoNavegacao.push(livro);
        // Mostra na tela qual livro foi visualizado.
        System.out.println("Livro visualizado: " + livro);
    }

    // Este metodo mostra o historico dos livros visualizados.
    public void exibirHistoricoNavegacao() {
        // Se ainda nao houver historico, avisa o usuario.
        if (historicoNavegacao.isEmpty()) {
            System.out.println("Nenhum livro foi visualizado ainda.");
            return;
        }

        // Mostra um titulo para a secao.
        System.out.println("Historico de navegacao:");
        // Percorre a pilha de tras para frente para mostrar do mais recente ao mais antigo.
        for (int i = historicoNavegacao.size() - 1; i >= 0; i--) {
            // Mostra o livro daquela posicao.
            System.out.println(historicoNavegacao.get(i));
        }
    }

    // Este metodo faz o emprestimo de um livro.
    public void emprestarLivro(String titulo, String usuario) {
        // Procura o livro pelo titulo.
        Livro livro = buscarLivroPorTitulo(titulo);
        // Se nao existir, avisa.
        if (livro == null) {
            System.out.println("Livro nao encontrado.");
            return;
        }

        // Se o livro estiver disponivel, ele e emprestado agora.
        if (!livro.isEmprestado()) {
            // Marca o livro como emprestado.
            livro.emprestar();
            // Mostra a mensagem de sucesso.
            System.out.println("Livro \"" + titulo + "\" emprestado para " + usuario + ".");
            return;
        }

        // Se o livro ja estiver emprestado, o usuario entra na fila de espera.
        livro.adicionarNaListaEspera(usuario);
        // Mostra a mensagem explicando que o usuario foi para a fila.
        System.out.println("Livro indisponivel. " + usuario + " foi adicionado(a) a lista de espera.");
    }

    // Este metodo registra a devolucao de um livro.
    public void devolverLivro(String titulo) {
        // Procura o livro pelo titulo.
        Livro livro = buscarLivroPorTitulo(titulo);
        // Se nao encontrar, avisa.
        if (livro == null) {
            System.out.println("Livro nao encontrado.");
            return;
        }

        // Se ele ja estiver disponivel, nao ha o que devolver.
        if (!livro.isEmprestado()) {
            System.out.println("O livro ja esta disponivel na biblioteca.");
            return;
        }

        // Se existir fila de espera, o proximo usuario recebe prioridade.
        if (livro.possuiListaEspera()) {
            // Pega o proximo nome da fila.
            String proximoUsuario = livro.chamarProximoDaFila();
            // Informa que houve devolucao.
            System.out.println("Livro \"" + titulo + "\" devolvido.");
            // Informa quem e o proximo da fila.
            System.out.println("Proximo usuario da fila: " + proximoUsuario + ".");
            // Explica que o livro foi reservado para essa pessoa.
            System.out.println("O livro foi reservado automaticamente para esse usuario.");
            return;
        }

        // Se nao houver fila, o livro volta a ficar disponivel.
        livro.devolver();
        // Mostra a mensagem final.
        System.out.println("Livro \"" + titulo + "\" devolvido e disponivel para emprestimo.");
    }

    // Este metodo mostra quem esta esperando por um livro.
    public void exibirListaEspera(String titulo) {
        // Procura o livro pelo titulo.
        Livro livro = buscarLivroPorTitulo(titulo);
        // Se nao encontrar, avisa.
        if (livro == null) {
            System.out.println("Livro nao encontrado.");
            return;
        }

        // Se nao houver fila, tambem avisa.
        if (!livro.possuiListaEspera()) {
            System.out.println("Nao ha usuarios na lista de espera desse livro.");
            return;
        }

        // Mostra o titulo da lista.
        System.out.println("Lista de espera do livro \"" + titulo + "\":");
        // Passa por cada nome da fila.
        for (String usuario : livro.getListaEspera()) {
            // Mostra o nome atual.
            System.out.println(usuario);
        }
    }

    // Este metodo liga dois livros no grafo de recomendacoes.
    // A ligacao e bidirecional, ou seja:
    // se A recomenda B, entao B tambem recomenda A.
    public void conectarLivros(String tituloOrigem, String tituloDestino) {
        // Procura o primeiro livro.
        Livro origem = buscarLivroPorTitulo(tituloOrigem);
        // Procura o segundo livro.
        Livro destino = buscarLivroPorTitulo(tituloDestino);

        // Se algum deles nao existir, nao e possivel criar a ligacao.
        if (origem == null || destino == null) {
            System.out.println("Nao foi possivel criar a recomendacao entre \"" + tituloOrigem
                    + "\" e \"" + tituloDestino + "\".");
            return;
        }

        // Adiciona o destino na lista de recomendacoes da origem.
        grafoRecomendacoes.get(origem).add(destino);
        // Adiciona a origem na lista de recomendacoes do destino.
        grafoRecomendacoes.get(destino).add(origem);
    }

    // Este metodo recomenda livros parecidos com um livro escolhido.
    public void recomendarPorLivro(String titulo) {
        // Procura o livro informado.
        Livro livro = buscarLivroPorTitulo(titulo);
        // Se nao existir, avisa.
        if (livro == null) {
            System.out.println("Livro nao encontrado.");
            return;
        }

        // Pega o conjunto de recomendacoes desse livro.
        // Se nao houver, usa um conjunto vazio.
        Set<Livro> recomendacoes = grafoRecomendacoes.getOrDefault(livro, new HashSet<>());
        // Se nao houver recomendacoes, avisa.
        if (recomendacoes.isEmpty()) {
            System.out.println("Nao ha recomendacoes cadastradas para \"" + titulo + "\".");
            return;
        }

        // Mostra a frase de apresentacao das sugestoes.
        System.out.println("Se voce gostou de \"" + livro.getTitulo() + "\", talvez goste de:");
        // Mostra os livros recomendados.
        exibirTitulos(recomendacoes);
    }

    // Este metodo usa o historico da pessoa para sugerir novos livros.
    public void recomendarPorHistorico() {
        // Se o historico estiver vazio, nao ha como sugerir.
        if (historicoNavegacao.isEmpty()) {
            System.out.println("Nenhum livro foi visualizado ainda. Nao ha historico para recomendar.");
            return;
        }

        // Aqui vamos guardar todas as recomendacoes encontradas.
        Set<Livro> recomendacoes = new HashSet<>();
        // Aqui guardamos os livros que a pessoa ja visualizou.
        Set<Livro> livrosLidos = new HashSet<>(historicoNavegacao);

        // Para cada livro do historico...
        for (Livro livroLido : historicoNavegacao) {
            // ...somamos as recomendacoes daquele livro ao conjunto principal.
            recomendacoes.addAll(grafoRecomendacoes.getOrDefault(livroLido, new HashSet<>()));
        }

        // Remove das recomendacoes os livros que a pessoa ja viu.
        recomendacoes.removeAll(livrosLidos);

        // Se nao sobrar nenhuma recomendacao nova, avisa.
        if (recomendacoes.isEmpty()) {
            System.out.println("O historico atual nao gerou novas recomendacoes.");
            return;
        }

        // Mostra o titulo da secao.
        System.out.println("Recomendacoes baseadas no historico de leitura:");
        // Exibe os livros recomendados.
        exibirTitulos(recomendacoes);
    }

    // Este metodo sugere livros com base em um interesse digitado pela pessoa.
    public void recomendarPorInteresse(String interesse) {
        // Normaliza o texto para facilitar a comparacao.
        String interesseNormalizado = normalizarTexto(interesse);
        // Cria um conjunto para guardar as sugestoes sem repetir.
        Set<Livro> sugestoes = new HashSet<>();

        // Percorre todos os livros cadastrados.
        for (Livro livro : livros) {
            // Pega o titulo atual normalizado.
            String titulo = normalizarTexto(livro.getTitulo());

            // Se o titulo do livro contiver o texto do interesse...
            if (titulo.contains(interesseNormalizado)) {
                // ...adiciona o proprio livro...
                sugestoes.add(livro);
                // ...e tambem adiciona os livros ligados a ele no grafo.
                sugestoes.addAll(grafoRecomendacoes.getOrDefault(livro, new HashSet<>()));
            }
        }

        // Se nao encontrou nada, avisa.
        if (sugestoes.isEmpty()) {
            System.out.println("Nenhum livro foi encontrado para o interesse \"" + interesse + "\".");
            return;
        }

        // Mostra o titulo da secao.
        System.out.println("Sugestoes para o interesse \"" + interesse + "\":");
        // Mostra as sugestoes encontradas.
        exibirTitulos(sugestoes);
    }

    // Este metodo aplica Dijkstra no grafo de livros e mostra os livros mais proximos.
    public void recomendarPorDijkstra(String tituloOrigem) {
        Livro origem = buscarLivroPorTitulo(tituloOrigem);

        if (origem == null) {
            System.out.println("Livro nao encontrado.");
            return;
        }

        Map<Livro, Integer> distancias = djikstraSimples(grafoRecomendacoes, origem);
        List<Map.Entry<Livro, Integer>> resultadosOrdenados = new ArrayList<>(distancias.entrySet());

        Collections.sort(resultadosOrdenados, new Comparator<Map.Entry<Livro, Integer>>() {
            @Override
            public int compare(Map.Entry<Livro, Integer> primeiro, Map.Entry<Livro, Integer> segundo) {
                int comparacaoDistancia = primeiro.getValue().compareTo(segundo.getValue());

                if (comparacaoDistancia != 0) {
                    return comparacaoDistancia;
                }

                return primeiro.getKey().getTitulo().compareToIgnoreCase(segundo.getKey().getTitulo());
            }
        });

        System.out.println("Livros mais proximos de \"" + origem.getTitulo() + "\" pelo algoritmo de Dijkstra:");

        boolean encontrouRecomendacao = false;
        for (Map.Entry<Livro, Integer> resultado : resultadosOrdenados) {
            Livro livro = resultado.getKey();
            int distancia = resultado.getValue();

            if (livro.equals(origem)) {
                continue;
            }

            encontrouRecomendacao = true;
            System.out.println("  * Distancia " + distancia + " -> "
                    + livro.getTitulo() + " - " + livro.getAutor());
        }

        if (!encontrouRecomendacao) {
            System.out.println("Nao existem outros livros conectados a este livro no grafo.");
        }
    }

    // Este algoritmo calcula a menor distancia entre a origem e cada livro conectado no grafo.
    // Como as ligacoes nao possuem peso, cada aresta vale 1.
    public static Map<Livro, Integer> djikstraSimples(HashMap<Livro, Set<Livro>> grafo, Livro origem) {
        Map<Livro, Integer> distancias = new HashMap<>();
        Queue<Livro> fila = new LinkedList<>();

        distancias.put(origem, 0);
        fila.add(origem);

        while (!fila.isEmpty()) {
            Livro atual = fila.poll();
            int distanciaAtual = distancias.get(atual);

            for (Livro vizinho : grafo.getOrDefault(atual, new HashSet<>())) {
                if (!distancias.containsKey(vizinho)) {
                    distancias.put(vizinho, distanciaAtual + 1);
                    fila.add(vizinho);
                }
            }
        }

        return distancias;
    }

    // Este metodo mostra o grafo completo na tela.
    public void exibirGrafoRecomendacoes() {
        // Mostra o titulo da secao.
        System.out.println("Grafo de recomendacoes:");
        // Passa por todos os livros cadastrados.
        for (Livro livro : livros) {
            // Mostra o nome do livro principal.
            System.out.println("- " + livro.getTitulo() + " ->");
            // Pega as recomendacoes desse livro.
            Set<Livro> recomendacoes = grafoRecomendacoes.getOrDefault(livro, new HashSet<>());
            // Exibe as recomendacoes.
            exibirTitulos(recomendacoes);
        }
    }

    // Este metodo auxiliar serve para mostrar uma lista de livros no console.
    private void exibirTitulos(Set<Livro> livrosParaExibir) {
        // Se o conjunto estiver vazio, avisa.
        if (livrosParaExibir.isEmpty()) {
            System.out.println("Nenhuma sugestao disponivel.");
            return;
        }

        // Percorre cada livro do conjunto.
        for (Livro livro : livrosParaExibir) {
            // Mostra o titulo e o autor do livro atual.
            System.out.println("  * " + livro.getTitulo() + " - " + livro.getAutor());
        }
    }

    // Este metodo auxiliar procura um livro pelo titulo.
    // Ele e privado porque so a propria classe precisa usar essa busca.
    private Livro buscarLivroPorTitulo(String titulo) {
        String tituloNormalizado = normalizarTexto(titulo);

        // Passa por todos os livros cadastrados.
        for (Livro livro : livros) {
            // Compara o titulo do livro atual com o titulo procurado.
            // A normalizacao ignora diferencas entre maiusculas, minusculas e acentos.
            if (normalizarTexto(livro.getTitulo()).equals(tituloNormalizado)) {
                // Se encontrar, devolve o livro.
                return livro;
            }
        }

        // Se terminar o laco sem encontrar, devolve null.
        return null;
    }

    // Este metodo verifica se todas as palavras digitadas aparecem no texto comparado.
    private boolean textoContemTodasAsPalavras(String textoNormalizado, String pesquisaNormalizada) {
        String[] palavrasPesquisadas = pesquisaNormalizada.split(" ");

        for (String palavra : palavrasPesquisadas) {
            if (!palavra.isEmpty() && !textoNormalizado.contains(palavra)) {
                return false;
            }
        }

        return true;
    }

    // Este metodo insere um livro na arvore binaria usando o titulo como criterio.
    private No inserirNaArvore(No atual, Livro livro) {
        if (atual == null) {
            return new No(livro);
        }

        int comparacao = livro.getTitulo().compareToIgnoreCase(atual.livro.getTitulo());
        if (comparacao < 0) {
            atual.esquerda = inserirNaArvore(atual.esquerda, livro);
        } else {
            atual.direita = inserirNaArvore(atual.direita, livro);
        }

        return atual;
    }

    // Este metodo procura o livro em profundidade.
    private Livro buscarLivroNaArvoreDFS(No atual, String titulo) {
        if (atual == null) {
            return null;
        }

        String tituloNormalizado = normalizarTexto(titulo);
        if (normalizarTexto(atual.livro.getTitulo()).equals(tituloNormalizado)) {
            return atual.livro;
        }

        Livro encontradoNaEsquerda = buscarLivroNaArvoreDFS(atual.esquerda, titulo);
        if (encontradoNaEsquerda != null) {
            return encontradoNaEsquerda;
        }

        return buscarLivroNaArvoreDFS(atual.direita, titulo);
    }

    // Este metodo registra o caminho da DFS ate encontrar o livro procurado.
    private boolean percorrerDFS(No atual, String titulo, List<String> percurso) {
        if (atual == null) {
            return false;
        }

        String tituloNormalizado = normalizarTexto(titulo);
        percurso.add(atual.livro.getTitulo());
        if (normalizarTexto(atual.livro.getTitulo()).equals(tituloNormalizado)) {
            return true;
        }

        return percorrerDFS(atual.esquerda, titulo, percurso)
                || percorrerDFS(atual.direita, titulo, percurso);
    }

    // Este metodo padroniza textos para facilitar pesquisas.
    private String normalizarTexto(String texto) {
        String textoSemAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return textoSemAcentos.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
