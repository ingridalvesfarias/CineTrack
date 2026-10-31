import java.time.LocalDate;
import java.util.List;
import model.Filme;
import service.FilmeService;

public class Main {
    public static void main(String[] args) {
        FilmeService service = new FilmeService();

        service.cadastrar("Interestelar", "Christopher Nolan", 2014, "Ficção científica", 169);
        service.cadastrar("Parasita", "Bong Joon-ho", 2019, "Suspense", 132);
        service.cadastrar("A Origem", "Christopher Nolan", 2010, "Ficção científica", 148);
        service.cadastrar("Duna: Parte Dois", "Denis Villeneuve", 2024, "Ficção científica", 166);

        imprimir("Coleção (ordem alfabética)", service.listarTodos());

        service.marcarComoAssistido(1, LocalDate.of(2026, 10, 20));
        service.avaliar(1, 9.5);
        service.adicionarComentario(1, "Trilha sonora incrível.");
        service.marcarComoAssistido(2, LocalDate.of(2026, 10, 25));
        service.avaliar(2, 10);

        imprimir("Assistidos", service.listarAssistidos());
        imprimir("Watchlist", service.listarWatchlist());
        imprimir("Busca por título 'origem'", service.buscarPorTitulo("origem"));
        imprimir("Gênero 'ficção científica'", service.filtrarPorGenero("ficção científica"));

        service.editar(3, "A Origem (Inception)", "Christopher Nolan", 2010, "Ficção científica", 148);
        imprimir("Após editar o ID 3", List.of(service.buscarPorId(3)));

        service.remover(4);
        System.out.println("Total após remover: " + service.quantidade());

        try {
            service.cadastrar("   ", "Alguém", 2000, "Drama", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        Filme novo = service.cadastrar("Matrix", "Irmãs Wachowski", 1999, "Ficção científica", 136);
        System.out.println("Novo filme recebeu o ID " + novo.getId());
        System.out.println("Total: " + service.quantidade());

        try {
            service.avaliar(3, 8); // filme 3 ainda não foi assistido
        } catch (IllegalStateException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        try {
            service.remover(99);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
    }

    private static void imprimir(String titulo, List<Filme> lista) {
        System.out.println("--- " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("(nenhum filme)");
        }
        for (Filme filme : lista) {
            System.out.println(filme);
        }
    }
}