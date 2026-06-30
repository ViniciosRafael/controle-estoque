package com.mycompany.contole_estoque;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                   CLASSE: PRODUTO PERECÍVEL                               ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Representa um produto que pode vencer/estragar (perecível).
 * Exemplos: alimentos (leite, queijo, carnes), bebidas, produtos congelados, etc.
 *
 * Características:
 *  • Tem data de validade por LOTE (não por produto)
 *  • Cada lote pode ter uma validade diferente
 *  • Monitora alertas de vencimento automático
 *  • Possibilita cálculo de prejuízo quando vence
 *
 * Atributos:
 *  • estoqueMinimo  : Quantidade mínima desejada em estoque
 *  • precoUnitario  : Valor de cada unidade do produto
 *
 * Funcionalidades:
 *  • Herda dados básicos da classe Produto (id, nome, categoria)
 *  • Calcula prejuízo baseado no preço unitário
 *  • Permite monitorar nível de estoque contra o mínimo
 *  • Integra com o sistema de alertas para produtos em vencimento
 */
public class ProdutoPerecivel extends Produto {
    private int    estoqueMinimo;  // Quantidade mínima desejada em estoque
    private double precoUnitario;  // Preço de cada unidade

    public ProdutoPerecivel() { super(); }

    /**
     * Construtor completo do Produto Perecível.
     *
     * @param id              : Identificador único do produto
     * @param nome            : Nome descritivo (ex: "Leite Integral")
     * @param categoria       : Classificação (ex: "Laticínios")
     * @param estoqueMinimo   : Quantidade mínima desejada
     * @param precoUnitario   : Valor de uma unidade
     */
    public ProdutoPerecivel(int id, String nome, String categoria, int estoqueMinimo, double precoUnitario) {
        super(id, nome, categoria);
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS PRINCIPAIS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Retorna o estoque mínimo configurado para este produto perecível.
     * @return Quantidade mínima desejada em estoque
     */
    @Override
    public int getEstoqueMinimo()  { return estoqueMinimo;  }

    /**
     * Retorna o preço unitário deste produto.
     * @return Preço de uma unidade em reais
     */
    public double getPrecoUnitario()  { return precoUnitario;  }

    // ═══════════════════════════════════════════════════════════════════
    // MODIFICADORES
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Modifica o estoque mínimo desejado.
     * @param estoqueMinimo Novo valor mínimo
     */
    public void setEstoqueMinimo(int estoqueMinimo)       { this.estoqueMinimo = estoqueMinimo; }

    /**
     * Modifica o preço unitário.
     * @param precoUnitario Novo preço por unidade
     */
    public void setPrecoUnitario(double precoUnitario)    { this.precoUnitario = precoUnitario; }

    // ═══════════════════════════════════════════════════════════════════
    // CÁLCULO DE PREJUÍZO
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Calcula o prejuízo causado pelo vencimento deste produto.
     * O prejuízo é igual ao preço unitário (uma unidade perdida = um prejuízo de R$ preço).
     *
     * Uso prático:
     *  • Quando um lote vence, o prejuízo total = quantidade vencida × calcularPrejuizo()
     *  • Exemplo: 50 unidades de leite a R$ 3,50 = prejuízo de R$ 175
     *
     * @return Prejuízo por unidade do produto (preço unitário)
     */
    @Override
    public double calcularPrejuizo() {
        return this.precoUnitario;
    }
}