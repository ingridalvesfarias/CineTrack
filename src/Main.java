import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Filme;
import service.FilmeService;
import util.Estatisticas;

public class Main {
    public static void main(String[] args) {
        FilmeService service = new FilmeService();

        // 1) Coleção vazia
        imprimirEstatisticas(service.listarTodos());

        // 2) Coleção com dados
        service.cadastrar("Interestelar", "Christopher Nolan", 2014, "Ficção científica", 169);
        service.cadastrar("Parasita", "Bong Joon-ho", 2019, "Suspense", 132);
        service.cadastrar("A Origem", "Christopher Nolan", 2010, "Ficção científica", 148);
        service.cadastrar("Duna: Parte Dois", "Denis Villeneuve", 2024, "ficção científica", 166);
        service.cadastrar("Matrix", "Irmãs Wachowski", 1999, "Ficção científica", 136);
        service.cadastrar("Clube da Luta", "David Fincher", 1999, "Drama", 139);
        service.cadastrar("O Poderoso Chefão", "Francis Ford Coppola", 1972, "Drama", 175);

        service.marcarComoAssistido(1, LocalDate.of(2026, 10, 20));
        service.avaliar(1, 9.5);
        service.marcarComoAssistido(2, LocalDate.of(2026, 10, 25));
        service.avaliar(2, 10);
        service.marcarComoAssistido(3, LocalDate.of(2026, 10, 27));
        service.avaliar(3, 9);
        service.marcarComoAssistido(6, LocalDate.of(2026, 10, 29)); // assistido, ainda sem nota

        imprimirEstatisticas(service.listarTodos());
    }

    private static void imprimirEstatisticas(List<Filme> filmes) {
        System.out.println("====== ESTATÍSTICAS DA COLEÇÃO ======");
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
        System.out.println("=====================================");
    }
}