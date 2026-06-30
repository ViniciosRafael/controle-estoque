package com.mycompany.contole_estoque;

/**
 * Classe que representa produtos com data de validade, requerendo controle rígido para evitar perdas por vencimento.
 */
public class ProdutoPerecivel extends Produto {
    private int    estoqueMinimo;  // Quantidade mínima desejada em estoque
    private double precoUnitario;  // Preço de cada unidade

    /**
     * Construtor padrão que inicializa a classe base.
     */
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

    /**
     * Obtém o estoque mínimo configurado para alertas.
     */
    @Override
    public int getEstoqueMinimo()  { return estoqueMinimo;  }

    /**
     * Obtém o preço unitário do produto.
     */
    public double getPrecoUnitario()  { return precoUnitario;  }

    /**
     * Define a nova quantidade para o alerta de estoque mínimo.
     */
    public void setEstoqueMinimo(int estoqueMinimo)       { this.estoqueMinimo = estoqueMinimo; }

    /**
     * Define o novo preço unitário.
     */
    public void setPrecoUnitario(double precoUnitario)    { this.precoUnitario = precoUnitario; }

    /**
     * Retorna o prejuízo financeiro causado pela perda de uma unidade, que reflete seu preço.
     */
    @Override
    public double calcularPrejuizo() {
        return this.precoUnitario;
    }
}