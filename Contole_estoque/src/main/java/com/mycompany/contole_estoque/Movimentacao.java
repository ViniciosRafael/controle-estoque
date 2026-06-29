package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 * Esta classe representa qualquer ação que mude o estoque.
 * Pode ser uma entrada (INCLUSÃO), uma saída comum (BAIXA) ou uma perda (DESCARTE).
 */
public class Movimentacao {
    
    // Tipos possíveis de movimentação
    public enum Tipo {
        INCLUSAO("Inclusão"),
        BAIXA("Baixa"),
        DESCARTE("Descarte");

        private final String descricao;
        Tipo(String d) { this.descricao = d; }
        public String getDescricao() { return descricao; }
    }

    private final int id;              // ID da movimentação
    private final Tipo tipo;           // Qual tipo de ação foi feita
    private final LoteEstoque lote;    // Em qual lote foi feita
    private final int quantidade;      // Quantas unidades foram afetadas
    private final LocalDate data;      // Quando aconteceu
    private final String observacao;   // Um comentário sobre a ação

    public Movimentacao(int id, Tipo tipo, LoteEstoque lote, int quantidade, LocalDate data, String observacao) {
        this.id = id;
        this.tipo = tipo;
        this.lote = lote;
        this.quantidade = quantidade;
        this.data = data;
        this.observacao = observacao;
    }

    // Getters para pegar as informações
    public int getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public LoteEstoque getLote() { return lote; }
    public int getQuantidade() { return quantidade; }
    public LocalDate getData() { return data; }
    public String getObservacao() { return observacao; }

    // Pega o nome do produto de forma fácil, mesmo que o lote não exista mais
    public String getNomeProduto() {
        return (lote != null && lote.getProduto() != null) ? lote.getProduto().getNome() : "—";
    }

    // Pega o número do lote de forma fácil
    public String getNumeroLote() {
        return (lote != null) ? lote.getNumeroLote() : "—";
    }
}
