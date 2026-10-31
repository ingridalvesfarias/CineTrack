package enums;

/**
 * Situação de um filme na coleção pessoal.
 */
public enum StatusFilme {

    ASSISTIDO("Assistido"),
    QUERO_ASSISTIR("Quero assistir");

    private final String descricao;

    StatusFilme(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}