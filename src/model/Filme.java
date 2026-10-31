package model;

import enums.StatusFilme;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

/**
 * Representa um filme da coleção pessoal.
 */
public class Filme {

    public static final int ANO_MINIMO = 1888; // ano dos primeiros filmes da história
    public static final int ANOS_FUTUROS_PERMITIDOS = 5; // lançamentos já anunciados
    public static final double NOTA_MINIMA = 0.0;
    public static final double NOTA_MAXIMA = 10.0;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private String titulo;
    private String diretor;
    private int anoLancamento;
    private String genero;
    private int duracaoMinutos;

    private Double nota;              // null = ainda sem nota
    private String comentario;        // null = ainda sem resenha
    private LocalDate dataAssistido;  // null = ainda não assistido
    private StatusFilme status;

    public Filme(int id, String titulo, String diretor, int anoLancamento,
                 String genero, int duracaoMinutos) {
        if (id < 1) {
            throw new IllegalArgumentException("O ID deve ser maior que zero.");
        }
        this.id = id;
        setTitulo(titulo);
        setDiretor(diretor);
        setAnoLancamento(anoLancamento);
        setGenero(genero);
        setDuracaoMinutos(duracaoMinutos);
        this.status = StatusFilme.QUERO_ASSISTIR; // todo filme novo começa na watchlist
    }

    // ===== Getters e setters =====

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = validarTexto(titulo, "Título");
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = validarTexto(diretor, "Diretor");
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        int anoMaximo = Year.now().getValue() + ANOS_FUTUROS_PERMITIDOS;
        if (anoLancamento < ANO_MINIMO || anoLancamento > anoMaximo) {
            throw new IllegalArgumentException(
                "Ano de lançamento deve estar entre " + ANO_MINIMO + " e " + anoMaximo + ".");
        }
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = validarTexto(genero, "Gênero");
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        if (duracaoMinutos < 1) {
            throw new IllegalArgumentException("Duração deve ser maior que zero.");
        }
        this.duracaoMinutos = duracaoMinutos;
    }

    public Double getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDate getDataAssistido() {
        return dataAssistido;
    }

    public StatusFilme getStatus() {
        return status;
    }

    // ===== Regras de negócio =====

    public boolean isAssistido() {
        return status == StatusFilme.ASSISTIDO;
    }

    public boolean possuiNota() {
        return nota != null;
    }

    public void marcarComoAssistido(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Informe a data em que o filme foi assistido.");
        }
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data em que foi assistido não pode estar no futuro.");
        }
        this.dataAssistido = data;
        this.status = StatusFilme.ASSISTIDO;
    }

    public void avaliar(double nota) {
        if (!isAssistido()) {
            throw new IllegalStateException("Só é possível avaliar filmes já assistidos.");
        }
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new IllegalArgumentException(
                "A nota deve estar entre " + NOTA_MINIMA + " e " + NOTA_MAXIMA + ".");
        }
        this.nota = nota;
    }

    public void adicionarComentario(String comentario) {
        this.comentario = validarTexto(comentario, "Comentário");
    }

    // ===== Métodos auxiliares =====

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%d] %s (%d) | Dir.: %s | %s | %d min | %s",
                id, titulo, anoLancamento, diretor, genero, duracaoMinutos, status.getDescricao()));
        if (dataAssistido != null) {
            sb.append(" em ").append(dataAssistido.format(FORMATO_DATA));
        }
        if (nota != null) {
            sb.append(String.format(" | Nota: %.1f", nota));
        }
        return sb.toString();
    }
}