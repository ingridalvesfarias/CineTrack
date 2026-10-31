import java.time.LocalDate;
import model.Filme;

public class Main {
    public static void main(String[] args) {
        Filme filme = new Filme(1, "Interestelar", "Christopher Nolan", 2014, "Ficção científica", 169);
        System.out.println(filme);

        // Avaliar antes de assistir: deve falhar
        try {
            filme.avaliar(9.5);
        } catch (IllegalStateException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // Fluxo correto
        filme.marcarComoAssistido(LocalDate.of(2026, 10, 20));
        filme.avaliar(9.5);
        filme.adicionarComentario("Trilha sonora incrível e uma história emocionante.");
        System.out.println(filme);
        System.out.println("Resenha: " + filme.getComentario());

        // Validações
        try {
            filme.avaliar(11);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        try {
            new Filme(2, "   ", "Alguém", 2000, "Drama", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        try {
            new Filme(3, "Filme do Futuro", "Alguém", 2100, "Ficção científica", 120);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        try {
            filme.marcarComoAssistido(LocalDate.now().plusDays(1));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
    }
}