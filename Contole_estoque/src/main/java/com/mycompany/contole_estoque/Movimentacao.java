package com.mycompany.contole_estoque;
import java.time.LocalDate;

public class Movimentacao {
    
    
    public enum Tipo {
        INCLUSAO("Inclusão"),    // Entrada de novo lote
        BAIXA("Baixa"),            // Retirada/venda
        DESCARTE("Descarte");      // Perda/descarte

        private final String descricao;  // Descrição legenda

        /**
         * Construtor do enum.
         * @param d : Descrição para exibir na UI
         */
        Tipo(String d) { this.descricao = d; }

        /**
         * Retorna a descrição legenda do tipo.
         * @return Descrição em português
         */
        public String getDescricao() { return descricao; }
    }



    private final int id;              // ID único desta movimentação
    private final Tipo tipo;           // Tipo: INCLUSÃO, BAIXA ou DESCARTE
    private final LoteEstoque lote;    // Lote afetado pela movimentação
    private final int quantidade;      // Número de unidades envolvidas
    private final LocalDate data;      // Data da ação
    private final String observacao;   // Comentários/notas adicionais


    /**
     * Construtor completo da Movimentação.
     * Todos os parâmetros são obrigatórios.
     */
    public Movimentacao(int id, Tipo tipo, LoteEstoque lote, int quantidade, LocalDate data, String observacao) {
        this.id = id;
        this.tipo = tipo;
        this.lote = lote;
        this.quantidade = quantidade;
        this.data = data;
        this.observacao = observacao;
    }

    /**
     * Recupera o ID único desta movimentação.
     * @return ID da movimentação
     */
    public int getId() { return id; }

    /**
     * Recupera o tipo de movimentação.
     * @return Tipo (INCLUSÃO, BAIXA ou DESCARTE)
     */
    public Tipo getTipo() { return tipo; }

    /**
     * Recupera o lote envolvido nesta movimentação.
     * @return LoteEstoque afetado
     */
    public LoteEstoque getLote() { return lote; }

    /**
     * Recupera a quantidade de unidades envolvidas.
     * @return Número de unidades
     */
    public int getQuantidade() { return quantidade; }

    /**
     * Recupera a data da movimentação.
     * @return Data da ação
     */
    public LocalDate getData() { return data; }

    /**
     * Recupera as observações/comentários desta movimentação.
     * @return Texto descritivo
     */
    public String getObservacao() { return observacao; }

   
    public String getNomeProduto() {
        return (lote != null && lote.getProduto() != null) ? lote.getProduto().getNome() : "—";
    }


    public String getNumeroLote() {
        return (lote != null) ? lote.getNumeroLote() : "—";
    }
}
