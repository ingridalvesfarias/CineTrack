package service;

import enums.StatusFilme;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Filme;

/**
 * Regras de negócio e armazenamento dos filmes.
 */
public class FilmeService {

    private final List<Filme> filmes = new ArrayList<>();
    private int proximoId = 1;

    // ===== Cadastro e consultas =====

    public Filme cadastrar(String titulo, String diretor, int anoLancamento,
                           String genero, int duracaoMinutos) {
        Filme filme = new Filme(proximoId, titulo, diretor, anoLancamento, genero, duracaoMinutos);
        filmes.add(filme);
        proximoId++; // só avança se o filme foi criado com sucesso
        return filme;
    }

    public List<Filme> listarTodos() {
        return ordenarPorTitulo(filmes);
    }

    public List<Filme> listarAssistidos() {
        return filtrarPorStatus(StatusFilme.ASSISTIDO);
    }

    public List<Filme> listarWatchlist() {
        return filtrarPorStatus(StatusFilme.QUERO_ASSISTIR);
    }

    public Filme buscarPorId(int id) {
        for (Filme filme : filmes) {
            if (filme.getId() == id) {
                return filme;
            }
        }
        return null;
    }

    public List<Filme> buscarPorTitulo(String trecho) {
        List<Filme> encontrados = new ArrayList<>();
        if (trecho == null || trecho.isBlank()) {
            return encontrados;
        }
        String termo = trecho.trim().toLowerCase();
        for (Filme filme : filmes) {
            if (filme.getTitulo().toLowerCase().contains(termo)) {
                encontrados.add(filme);
            }
        }
        return ordenarPorTitulo(encontrados);
    }

    public List<Filme> filtrarPorGenero(String genero) {
        List<Filme> encontrados = new ArrayList<>();
        if (genero == null || genero.isBlank()) {
            return encontrados;
        }
        for (Filme filme : filmes) {
            if (filme.getGenero().equalsIgnoreCase(genero.trim())) {
                encontrados.add(filme);
            }
        }
        return ordenarPorTitulo(encontrados);
    }

    public int quantidade() {
        return filmes.size();
    }

    // ===== Operações sobre um filme existente =====

    public void marcarComoAssistido(int id, LocalDate data) {
        obterOuFalhar(id).marcarComoAssistido(data);
    }

    public void avaliar(int id, double nota) {
        obterOuFalhar(id).avaliar(nota);
    }

    public void adicionarComentario(int id, String comentario) {
        obterOuFalhar(id).adicionarComentario(comentario);
    }

    public void editar(int id, String titulo, String diretor, int anoLancamento,
                       String genero, int duracaoMinutos) {
        Filme filme = obterOuFalhar(id);
        filme.setTitulo(titulo);
        filme.setDiretor(diretor);
        filme.setAnoLancamento(anoLancamento);
        filme.setGenero(genero);
        filme.setDuracaoMinutos(duracaoMinutos);
    }

    public void remover(int id) {
        filmes.remove(obterOuFalhar(id));
    }

    // ===== Métodos auxiliares =====

    private Filme obterOuFalhar(int id) {
        Filme filme = buscarPorId(id);
        if (filme == null) {
            throw new IllegalArgumentException("Filme não encontrado: ID " + id);
        }
        return filme;
    }

    private List<Filme> filtrarPorStatus(StatusFilme status) {
        List<Filme> encontrados = new ArrayList<>();
        for (Filme filme : filmes) {
            if (filme.getStatus() == status) {
                encontrados.add(filme);
            }
        }
        return ordenarPorTitulo(encontrados);
    }

    // Devolve uma CÓPIA ordenada, sem mexer na lista original
    private List<Filme> ordenarPorTitulo(List<Filme> lista) {
        List<Filme> copia = new ArrayList<>(lista);
        copia.sort(Comparator.comparing(Filme::getTitulo, String.CASE_INSENSITIVE_ORDER));
        return copia;
    }
}