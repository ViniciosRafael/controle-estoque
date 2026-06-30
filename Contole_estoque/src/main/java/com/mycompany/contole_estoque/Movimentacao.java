package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                    CLASSE: HISTÓRICO DE MOVIMENTAÇÕES                   ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Registra cada ação que altera a quantidade de um lote no estoque.
 * É um histórico completo e imútavel de todas as operações.
 *
 * Tipos de movimentação:
 *  • INCLUSÃO: Entrada de um novo lote no estoque
 *  • BAIXA    : Retirada de produtos do estoque (venda, transferência, etc)
 *  • DESCARTE : Perda de produtos (vencimento, dano, roubo, etc)
 *
 * Características:
 *  • Todos os atributos são imutáveis (final) - registro é à prova de modificação
 *  • Permite rastreamento completo de qualquer lote
 *  • Possibilita audiêcia e conformidade regulatória
 *  • Facilita análises de padrões de movimentação
 *
 * Atributos:
 *  • id         : ID único desta movimentação
 *  • tipo       : Tipo de ação (INCLUSÃO, BAIXA ou DESCARTE)
 *  • lote       : Referência para o LoteEstoque afetado
 *  • quantidade : Quantidade de unidades envolvidas
 *  • data       : Data em que a ação foi realizada
 *  • observacao : Notas/comentarios sobre a movimentação
 */
public class Movimentacao {
    
    /**
     * Enum que define os três tipos possíveis de movimentação de estoque.
     * Cada tipo tem uma descrição legenda que é exibida na interface.
     */
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

    // ═══════════════════════════════════════════════════════════════════
    // ATRIBUTOS (IMUTÁVEIS)
    // ═══════════════════════════════════════════════════════════════════

    private final int id;              // ID único desta movimentação
    private final Tipo tipo;           // Tipo: INCLUSÃO, BAIXA ou DESCARTE
    private final LoteEstoque lote;    // Lote afetado pela movimentação
    private final int quantidade;      // Número de unidades envolvidas
    private final LocalDate data;      // Data da ação
    private final String observacao;   // Comentários/notas adicionais

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUTOR
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Construtor completo da Movimentação.
     * Todos os parâmetros são obrigatórios.
     *
     * @param id          : ID único desta movimentação
     * @param tipo        : Tipo de ação (INCLUSÃO, BAIXA, DESCARTE)
     * @param lote        : LoteEstoque envolvido
     * @param quantidade  : Quantidade de unidades
     * @param data        : Data da movimentação
     * @param observacao  : Notas/comentários adicionais
     */
    public Movimentacao(int id, Tipo tipo, LoteEstoque lote, int quantidade, LocalDate data, String observacao) {
        this.id = id;
        this.tipo = tipo;
        this.lote = lote;
        this.quantidade = quantidade;
        this.data = data;
        this.observacao = observacao;
    }

    // ═══════════════════════════════════════════════════════════════════
    // GETTERS (SÓ LEITURA - NÃO HÁ SETTERS)
    // ═══════════════════════════════════════════════════════════════════

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

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS AUXÍLIARES
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Recupera o nome do produto de forma segura.
     * Se o lote ou o produto foi removido do sistema, retorna "—" (travessão).
     * Ùtil para exibir histórico mesmo quando produtos são deletados.
     *
     * @return Nome do produto ou "—" se indisponível
     */
    public String getNomeProduto() {
        return (lote != null && lote.getProduto() != null) ? lote.getProduto().getNome() : "—";
    }

    /**
     * Recupera o número/código do lote de forma segura.
     * Se o lote foi removido do sistema, retorna "—" (travessão).
     *
     * @return Número do lote ou "—" se indisponível
     */
    public String getNumeroLote() {
        return (lote != null) ? lote.getNumeroLote() : "—";
    }
}
