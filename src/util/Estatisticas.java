package util;

import enums.StatusFilme;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import model.Filme;

/**
 * Classe utilitária: cálculos estatísticos sobre a coleção de filmes.
 */
public final class Estatisticas {

    // Construtor privado: classe só com métodos estáticos
    private Estatisticas() {
    }

    public static int contarAssistidos(List<Filme> filmes) {
        return contarPorStatus(filmes, StatusFilme.ASSISTIDO);
    }

    public static int contarWatchlist(List<Filme> filmes) {
        return contarPorStatus(filmes, StatusFilme.QUERO_ASSISTIR);
    }

    /**
     * Média das notas pessoais. Devolve vazio se nenhum filme foi avaliado.
     */
    public static Optional<Double> calcularMediaNotas(List<Filme> filmes) {
        double soma = 0;
        int quantidade = 0;

        for (Filme filme : filmes) {
            if (filme.possuiNota()) {
                soma += filme.getNota();
                quantidade++;
            }
        }

        if (quantidade == 0) {
            return Optional.empty();
        }
        return Optional.of(soma / quantidade);
    }

    /**
     * Filme com a maior nota. Em caso de empate, vale o primeiro da lista.
     * Devolve vazio se nenhum filme foi avaliado.
     */
    public static Optional<Filme> filmeMaisBemAvaliado(List<Filme> filmes) {
        Filme melhor = null;

        for (Filme filme : filmes) {
            if (!filme.possuiNota()) {
                continue;
            }
            if (melhor == null || filme.getNota() > melhor.getNota()) {
                melhor = filme;
            }
        }
        return Optional.ofNullable(melhor);
    }

    /**
     * Quantidade de filmes por gênero, em ordem alfabética.
     * "Drama" e "drama" contam como o mesmo gênero.
     */
    public static Map<String, Integer> contarPorGenero(List<Filme> filmes) {
        Map<String, Integer> contagem = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Filme filme : filmes) {
            String genero = filme.getGenero();
            contagem.put(genero, contagem.getOrDefault(genero, 0) + 1);
        }
        return contagem;
    }

    // ===== Método auxiliar =====

    private static int contarPorStatus(List<Filme> filmes, StatusFilme status) {
        int total = 0;
        for (Filme filme : filmes) {
            if (filme.getStatus() == status) {
                total++;
            }
        }
        return total;
    }
}