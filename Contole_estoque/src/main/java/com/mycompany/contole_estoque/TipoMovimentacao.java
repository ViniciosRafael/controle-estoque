package com.mycompany.contole_estoque;


public enum TipoMovimentacao {
    INCLUSAO("Inclusão"),    // Entrada de novo lote
    BAIXA("Baixa"),            // Retirada/venda
    DESCARTE("Descarte");      // Perda/descarte

    private final String descricao;  // Descrição legenda

    /**
     * Construtor do enum.
     * @param d : Descrição para exibir na UI
     */
    TipoMovimentacao(String d) { this.descricao = d; }

    /**
     * Retorna a descrição legenda do tipo.
     * @return Descrição em português
     */
    public String getDescricao() { return descricao; }
}
