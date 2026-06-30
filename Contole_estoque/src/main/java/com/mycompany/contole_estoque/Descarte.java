package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                       CLASSE: REGISTRO DE DESCARTE                         ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Registra um evento de descarte (perda) de produtos do estoque.
 * Um descarte ocorre quando produtos são removidos do estoque e perdidos/descartados.
 *
 * Motivos comuns de descarte:
 *  • Prazo de validade expirado (principal para perecíveis)
 *  • Avaria física ou dano
 *  • Contaminacção
 *  • Roubo ou extravio
 *  • Rejeicção de qualidade
 *
 * Atributos:
 *  • descarteId               : ID único deste registro de descarte
 *  • lote                      : Referência para o LoteEstoque que foi descartado
 *  • quantidadeDescartada     : Quantidade de unidades descartadas
 *  • dataDescarte             : Data em que o descarte foi registrado
 *  • motivo                    : Razão do descarte (texto descritivo)
 *
 * Responsabilidades:
 *  • Registrar e documentar perdas de produtos
 *  • Calcular o prejuízo financeiro causado pelo descarte
 *  • Atualizar o estoque (dar baixa) quando descarte é realizado
 *  • Manter auditoria de todos os descartes
 */
public class Descarte {
    private int descarteId;                    // ID único deste descarte
    private LoteEstoque lote;                  // Lote que foi descartado
    private int quantidadeDescartada;          // Quantidade de unidades perdidas
    private LocalDate dataDescarte;            // Data do descarte
    private String motivo;                     // Motivo do descarte (ex: "Prazo de validade expirado")

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUTORES
    // ═══════════════════════════════════════════════════════════════════

    /** Construtor vazio - usado para desserialização ou criação post-hoc */
    public Descarte() {}

    /**
     * Construtor completo do Descarte.
     *
     * @param descarteId            : ID único deste registro
     * @param lote                  : LoteEstoque que foi descartado
     * @param quantidadeDescartada  : Quantidade de unidades perdidas
     * @param dataDescarte          : Data do descarte
     * @param motivo                : Motivo/razão do descarte
     */
    public Descarte(int descarteId, LoteEstoque lote, int quantidadeDescartada, LocalDate dataDescarte, String motivo) {
        this.descarteId = descarteId;
        this.lote = lote;
        this.quantidadeDescartada = quantidadeDescartada;
        this.dataDescarte = dataDescarte;
        this.motivo = motivo;
    }

    // ═══════════════════════════════════════════════════════════════════
    // CÁLCULO DE PREJUÍZO
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Calcula o prejuízo financeiro total causado por este descarte.
     * Fórmula: Prejuízo = (Preço Unitário do Produto) × (Quantidade Descartada)
     *
     * Exemplos:
     *  • 50 unidades de Leite (R$ 3,50/un) vencido = R$ 175 de prejuízo
     *  • 10 unidades de Detergente (R$ 5,00/un) danificado = R$ 50 de prejuízo
     *
     * Nota: Se o produto foi removido do sistema (lote órfão),
     * retorna 0 pois não é possível calcular prejuízo sem referência de preço.
     *
     * @return Valor total do prejuízo em reais (R$)
     */
    public double calcularPrejuizo() {
        Produto produto = this.lote.getProduto();
        if (produto == null) return 0.0; // Lote órfão: sem produto não há como calcular
        double prejuizoUnitario = produto.calcularPrejuizo(); // Preço unitário
        return prejuizoUnitario * this.quantidadeDescartada;  // Total
    }

    // ═══════════════════════════════════════════════════════════════════
    // REGISTRO E AUDITORIA
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Registra este descarte no sistema.
     * Ao registrar:
     *  1. Aplica a baixa de quantidade no lote
     *  2. Exibe um relatório detalhado do descarte
     *  3. Mostra o prejuízo calculado
     *
     * Este método é chamado quando o descarte é confirmado pelo usuário.
     */
    public void registrar() {
        // Aplica a baixa no lote
        this.lote.darBaixa(this.quantidadeDescartada);

        // Exibe relatório de auditoria no console
        Produto produto = this.lote.getProduto();
        System.out.println("REGISTRO DE DESCARTE");
        System.out.println("Motivo: " + this.motivo);
        System.out.println("Produto: " + (produto != null ? produto.getNome() : "(produto removido)"));
        System.out.println("Quantidade: " + this.quantidadeDescartada);
        System.out.println("Prejuízo Total: " + this.calcularPrejuizo());
    }

    // ═══════════════════════════════════════════════════════════════════
    // GETTERS E SETTERS
    // ═══════════════════════════════════════════════════════════════════

    public int getDescarteId(){return descarteId;}
    public LoteEstoque getLote(){ return lote; }
    public int getQuantidadeDescartada() { return quantidadeDescartada; }
    public LocalDate getDataDescarte(){ return dataDescarte; }
    public String getMotivo(){ return motivo;}

    public void setDescarteId(int descarteId){ this.descarteId= descarteId; }
    public void setLote(LoteEstoque lote){ this.lote= lote; }
    public void setQuantidadeDescartada(int quantidadeDescartada){ this.quantidadeDescartada = quantidadeDescartada; }
    public void setDataDescarte(LocalDate dataDescarte){ this.dataDescarte = dataDescarte; }
    public void setMotivo(String motivo){ this.motivo= motivo; }
}