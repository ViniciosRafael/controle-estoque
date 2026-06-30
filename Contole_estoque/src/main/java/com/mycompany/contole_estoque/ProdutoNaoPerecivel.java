package com.mycompany.contole_estoque;


/**
 * Classe que representa um produto de estoque sem validade, como limpeza e utensílios.
 */
public class ProdutoNaoPerecivel extends Produto {

    private double precoUnitario;  // Preço de cada unidade
    private int    estoqueMinimo;  // Quantidade mínima desejada em estoque

    /**
     * Construtor padrão que inicializa a classe base.
     */
    public ProdutoNaoPerecivel() { super(); }

    /**
     * Construtor completo do Produto Não Perecível.
     *
     * @param id              : Identificador único do produto
     * @param nome            : Nome descritivo (ex: "Detergente Neutro")
     * @param categoria       : Classificação (ex: "Limpeza")
     * @param precoUnitario   : Valor de uma unidade
     * @param estoqueMinimo   : Quantidade mínima desejada
     */
    public ProdutoNaoPerecivel(int id, String nome, String categoria,
                               double precoUnitario, int estoqueMinimo) {
        super(id, nome, categoria);
        this.precoUnitario = precoUnitario;
        this.estoqueMinimo = estoqueMinimo;
    }



    /**
     * Retorna o prejuízo, que neste caso é exatamente o custo unitário do produto.
     */
    @Override
    public double calcularPrejuizo() { return precoUnitario; }

    /**
     * Retorna o nome deste produto.
     * @return Nome do produto
     */
    @Override
    public String getNome() { return super.getNome(); }

    /**
     * Registra este produto não perecível no sistema.
     * Exibe mensagem detalhada com todas as informações do produto.
     */
    @Override
    public void cadastrar() {
        System.out.println("Produto não perecível '" + getNome()
            + "' (categoria: " + getCategoria()
            + ") cadastrado. Preço unitário: R$ " + precoUnitario
            + " | Est. Mínimo: " + estoqueMinimo);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GETTERS E SETTERS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Retorna o preço unitário deste produto.
     * @return Preço de uma unidade em reais
     */
    public double getPrecoUnitario()                    { return precoUnitario; }

    /**
     * Modifica o preço unitário.
     * @param precoUnitario Novo preço por unidade
     */
    public void   setPrecoUnitario(double precoUnitario){ this.precoUnitario = precoUnitario; }

    /**
     * Retorna o estoque mínimo configurado para este produto.
     * @return Quantidade mínima desejada em estoque
     */
    @Override
    public int  getEstoqueMinimo()               { return estoqueMinimo; }

    /**
     * Modifica o estoque mínimo desejado.
     * @param estoqueMinimo Novo valor mínimo
     */
    public void setEstoqueMinimo(int estoqueMinimo){ this.estoqueMinimo = estoqueMinimo; }
}