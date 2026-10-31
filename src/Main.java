import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import model.Filme;
import service.FilmeService;
import util.Estatisticas;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FilmeService service = new FilmeService();

    private static final DateTimeFormatter FORMATO_EXIBICAO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_ENTRADA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static void main(String[] args) {
        boolean executando = true;

        while (executando) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opção: ");
            System.out.println();

            try {
                switch (opcao) {
                    case 1 -> cadastrarFilme();
                    case 2 -> mostrarColecao();
                    case 3 -> pesquisarFilme();
                    case 4 -> filtrarPorGenero();
                    case 5 -> avaliarFilme();
                    case 6 -> adicionarResenha();
                    case 7 -> marcarComoAssistido();
                    case 8 -> mostrarWatchlist();
                    case 9 -> editarFilme();
                    case 10 -> removerFilme();
                    case 11 -> mostrarEstatisticas();
                    case 0 -> {
                        System.out.println("Obrigada por usar o CineTrack. Bom filme! 🎬");
                        executando = false;
                    }
                    default -> erro("Opção inválida. Escolha um número de 0 a 11.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                erro(e.getMessage());
            }
        }

        scanner.close();
    }

    // ===================== MENU =====================

    private static void exibirMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("              CINETRACK");
        System.out.println("          PERSONAL MOVIE TRACKER");
        System.out.println("========================================");
        System.out.println();
        System.out.println("[1] Cadastrar filme");
        System.out.println("[2] Minha coleção");
        System.out.println("[3] Pesquisar filme");
        System.out.println("[4] Filtrar por gênero");
        System.out.println("[5] Avaliar filme");
        System.out.println("[6] Adicionar resenha");
        System.out.println("[7] Marcar como assistido");
        System.out.println("[8] Minha watchlist");
        System.out.println("[9] Editar filme");
        System.out.println("[10] Remover filme");
        System.out.println("[11] Estatísticas");
        System.out.println("[0] Sair");
        System.out.println();
    }

    // ===================== FUNCIONALIDADES =====================

    private static void cadastrarFilme() {
        titulo("CADASTRAR FILME");
        String titulo = lerTexto("Título: ");
        String diretor = lerTexto("Diretor: ");
        int ano = lerInteiro("Ano de lançamento: ");
        String genero = lerTexto("Gênero: ");
        int duracao = lerInteiro("Duração (minutos): ");

        Filme filme = service.cadastrar(titulo, diretor, ano, genero, duracao);
        sucesso("Filme cadastrado com ID " + filme.getId() + ".");

        if (confirmar("Você já assistiu a este filme?")) {
            LocalDate data = lerData("Data em que assistiu (dd/mm/aaaa, Enter = hoje): ");
            service.marcarComoAssistido(filme.getId(), data);
            sucesso("Filme marcado como assistido.");

            if (confirmar("Deseja dar uma nota agora?")) {
                service.avaliar(filme.getId(), lerNota());
                sucesso("Nota registrada.");
            }
        } else {
            aviso("Filme adicionado à sua watchlist.");
        }
    }

    private static void mostrarColecao() {
        titulo("MINHA COLEÇÃO");
        List<Filme> lista = service.listarTodos();

        if (lista.isEmpty()) {
            aviso("Sua coleção está vazia. Use a opção [1] para cadastrar um filme.");
            return;
        }
        imprimirTabela(lista);
        System.out.println("Total: " + lista.size() + " filme(s)");
    }

    private static void pesquisarFilme() {
        titulo("PESQUISAR FILME");
        String termo = lerTexto("Digite parte do título: ");

        List<Filme> resultados = service.buscarPorTitulo(termo);
        if (resultados.isEmpty()) {
            aviso("Nenhum filme encontrado para \"" + termo + "\".");
            return;
        }
        for (Filme filme : resultados) {
            imprimirFicha(filme);
        }
    }

    private static void filtrarPorGenero() {
        titulo("FILTRAR POR GÊNERO");
        Map<String, Integer> generos = Estatisticas.contarPorGenero(service.listarTodos());

        if (generos.isEmpty()) {
            aviso("Nenhum filme cadastrado ainda.");
            return;
        }
        System.out.println("Gêneros disponíveis: " + String.join(", ", generos.keySet()));
        String genero = lerTexto("Gênero: ");

        List<Filme> resultados = service.filtrarPorGenero(genero);
        if (resultados.isEmpty()) {
            aviso("Nenhum filme do gênero \"" + genero + "\".");
        } else {
            imprimirTabela(resultados);
        }
    }

    private static void avaliarFilme() {
        titulo("AVALIAR FILME");
        Filme filme = pedirFilme();
        if (filme == null) {
            return;
        }

        if (!filme.isAssistido()) {
            erro("Só é possível avaliar filmes já assistidos. Use a opção [7] primeiro.");
            return;
        }
        if (filme.possuiNota()) {
            aviso(String.format("Nota atual: %.1f (será substituída).", filme.getNota()));
        }

        service.avaliar(filme.getId(), lerNota());
        sucesso("Nota registrada!");
    }

    private static void adicionarResenha() {
        titulo("ADICIONAR RESENHA");
        Filme filme = pedirFilme();
        if (filme == null) {
            return;
        }

        if (filme.getComentario() != null) {
            aviso("Resenha atual: " + filme.getComentario());
            aviso("A nova resenha vai substituir a atual.");
        }

        String texto = lerTexto("Sua resenha: ");
        service.adicionarComentario(filme.getId(), texto);
        sucesso("Resenha salva!");
    }

    private static void marcarComoAssistido() {
        titulo("MARCAR COMO ASSISTIDO");
        Filme filme = pedirFilme();
        if (filme == null) {
            return;
        }

        if (filme.isAssistido()) {
            aviso("Este filme já foi assistido em "
                    + filme.getDataAssistido().format(FORMATO_EXIBICAO) + ".");
            if (!confirmar("Deseja alterar a data?")) {
                return;
            }
        }

        LocalDate data = lerData("Data em que assistiu (dd/mm/aaaa, Enter = hoje): ");
        service.marcarComoAssistido(filme.getId(), data);
        sucesso("Filme marcado como assistido.");

        if (!filme.possuiNota() && confirmar("Deseja dar uma nota agora?")) {
            service.avaliar(filme.getId(), lerNota());
            sucesso("Nota registrada.");
        }
    }

    private static void mostrarWatchlist() {
        titulo("MINHA WATCHLIST");
        List<Filme> lista = service.listarWatchlist();

        if (lista.isEmpty()) {
            aviso("Sua watchlist está vazia.");
            return;
        }
        imprimirTabela(lista);
        System.out.println("Total: " + lista.size() + " filme(s) para assistir");
    }

    private static void editarFilme() {
        titulo("EDITAR FILME");
        Filme filme = pedirFilme();
        if (filme == null) {
            return;
        }

        System.out.println("Pressione Enter para manter o valor atual.");
        String titulo = lerTextoOuManter("Título", filme.getTitulo());
        String diretor = lerTextoOuManter("Diretor", filme.getDiretor());
        int ano = lerInteiroOuManter("Ano", filme.getAnoLancamento());
        String genero = lerTextoOuManter("Gênero", filme.getGenero());
        int duracao = lerInteiroOuManter("Duração (min)", filme.getDuracaoMinutos());

        service.editar(filme.getId(), titulo, diretor, ano, genero, duracao);
        sucesso("Filme atualizado!");
    }

    private static void removerFilme() {
        titulo("REMOVER FILME");
        Filme filme = pedirFilme();
        if (filme == null) {
            return;
        }

        if (confirmar("Remover \"" + filme.getTitulo() + "\"?")) {
            service.remover(filme.getId());
            sucesso("Filme removido.");
        } else {
            aviso("Remoção cancelada.");
        }
    }

    private static void mostrarEstatisticas() {
        titulo("ESTATÍSTICAS DA COLEÇÃO");
        List<Filme> filmes = service.listarTodos();

        System.out.println("Total de filmes:      " + filmes.size());
        System.out.println("Assistidos:           " + Estatisticas.contarAssistidos(filmes));
        System.out.println("Na watchlist:         " + Estatisticas.contarWatchlist(filmes));

        Optional<Double> media = Estatisticas.calcularMediaNotas(filmes);
        if (media.isPresent()) {
            System.out.printf("Média das notas:      %.2f%n", media.get());
        } else {
            System.out.println("Média das notas:      sem notas ainda");
        }

        Optional<Filme> melhor = Estatisticas.filmeMaisBemAvaliado(filmes);
        if (melhor.isPresent()) {
            System.out.printf("Mais bem avaliado:    %s (%.1f)%n",
                    melhor.get().getTitulo(), melhor.get().getNota());
        } else {
            System.out.println("Mais bem avaliado:    nenhum filme avaliado");
        }

        System.out.println("Filmes por gênero:");
        Map<String, Integer> porGenero = Estatisticas.contarPorGenero(filmes);
        if (porGenero.isEmpty()) {
            System.out.println("  (nenhum)");
        }
        for (Map.Entry<String, Integer> par : porGenero.entrySet()) {
            System.out.println("  " + par.getKey() + ": " + par.getValue());
        }
    }

    // ===================== LEITURA DE DADOS =====================

    // Pede o ID e devolve o filme (ou null, já mostrando o erro)
    private static Filme pedirFilme() {
        int id = lerInteiro("ID do filme: ");
        Filme filme = service.buscarPorId(id);
        if (filme == null) {
            erro("Filme não encontrado: ID " + id);
            return null;
        }
        System.out.println("Filme: " + filme.getTitulo() + " (" + filme.getAnoLancamento() + ")");
        return filme;
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            erro("Este campo não pode ficar vazio.");
        }
    }

    private static String lerTextoOuManter(String campo, String atual) {
        System.out.print(campo + " [" + atual + "]: ");
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() ? atual : entrada;
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                erro("Digite um número inteiro válido.");
            }
        }
    }

    private static int lerInteiroOuManter(String campo, int atual) {
        while (true) {
            System.out.print(campo + " [" + atual + "]: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return atual;
            }
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                erro("Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(entrada);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    throw new NumberFormatException();
                }
                return valor;
            } catch (NumberFormatException e) {
                erro("Digite um número válido (exemplo: 8.5 ou 8,5).");
            }
        }
    }

    private static double lerNota() {
        while (true) {
            double nota = lerDouble("Nota (0 a 10): ");
            if (nota >= Filme.NOTA_MINIMA && nota <= Filme.NOTA_MAXIMA) {
                return nota;
            }
            erro("A nota deve estar entre 0 e 10.");
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return LocalDate.now();
            }
            try {
                LocalDate data = LocalDate.parse(entrada, FORMATO_ENTRADA);
                if (data.isAfter(LocalDate.now())) {
                    erro("A data não pode estar no futuro.");
                    continue;
                }
                return data;
            } catch (DateTimeParseException e) {
                erro("Data inválida. Use o formato dd/mm/aaaa (exemplo: 25/10/2026).");
            }
        }
    }

    private static boolean confirmar(String pergunta) {
        while (true) {
            System.out.print(pergunta + " (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            erro("Responda apenas com s ou n.");
        }
    }

    // ===================== EXIBIÇÃO =====================

    private static void imprimirTabela(List<Filme> lista) {
        System.out.printf("%-4s %-30s %-6s %-20s %-8s %-15s %s%n",
                "ID", "TÍTULO", "ANO", "GÊNERO", "DURAÇÃO", "STATUS", "NOTA");
        System.out.println("-".repeat(94));
        for (Filme f : lista) {
            String nota = f.possuiNota() ? String.format("%.1f", f.getNota()) : "-";
            System.out.printf("%-4d %-30s %-6d %-20s %-8s %-15s %s%n",
                    f.getId(),
                    cortar(f.getTitulo(), 30),
                    f.getAnoLancamento(),
                    cortar(f.getGenero(), 20),
                    f.getDuracaoMinutos() + " min",
                    f.getStatus().getDescricao(),
                    nota);
        }
    }

    private static void imprimirFicha(Filme f) {
        System.out.println("------------------------------------");
        System.out.println("[" + f.getId() + "] " + f.getTitulo());
        System.out.println("Diretor:  " + f.getDiretor());
        System.out.println("Ano:      " + f.getAnoLancamento());
        System.out.println("Gênero:   " + f.getGenero());
        System.out.println("Duração:  " + f.getDuracaoMinutos() + " min");

        String status = f.getStatus().getDescricao();
        if (f.getDataAssistido() != null) {
            status += " em " + f.getDataAssistido().format(FORMATO_EXIBICAO);
        }
        System.out.println("Status:   " + status);
        System.out.println("Nota:     " + (f.possuiNota() ? String.format("%.1f", f.getNota()) : "sem nota"));
        System.out.println("Resenha:  " + (f.getComentario() != null ? f.getComentario() : "sem resenha"));
    }

    // Corta textos longos para não desalinhar a tabela
    private static String cortar(String texto, int limite) {
        if (texto.length() <= limite) {
            return texto;
        }
        return texto.substring(0, limite - 3) + "...";
    }

    private static void titulo(String texto) {
        System.out.println("------------------------------------");
        System.out.println(" " + texto);
        System.out.println("------------------------------------");
    }

    private static void sucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    private static void erro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }

    private static void aviso(String mensagem) {
        System.out.println("[!] " + mensagem);
    }
}