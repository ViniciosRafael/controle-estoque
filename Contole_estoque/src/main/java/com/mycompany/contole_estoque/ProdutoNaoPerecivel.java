package com.mycompany.contole_estoque;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                   CLASSE: PRODUTO NÃO PERECÍVEL                           ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Representa um produto que NÃO vence/estraga (não perecível).
 * Exemplos: produtos de limpeza, higiene, mercearia, eletrônicos, papelaria, etc.
 *
 * Características:
 *  • NÃO tem data de validade
 *  • Lotes não têm data de vencimento
 *  • Monitora apenas alertas de estoque mínimo (não de vencimento)
 *  • Possibilita cálculo de prejuízo quando descartado/perdido
 *
 * Atributos:
 *  • precoUnitario : Valor de cada unidade do produto
 *  • estoqueMinimo : Quantidade mínima desejada em estoque
 *
 * Funcionalidades:
 *  • Herda dados básicos da classe Produto (id, nome, categoria)
 *  • Calcula prejuízo baseado no preço unitário
 *  • Permite monitorar nível de estoque contra o mínimo
 *  • Integra com o sistema de alertas para produtos com estoque baixo
 */
public class ProdutoNaoPerecivel extends Produto {

    private double precoUnitario;  // Preço de cada unidade
    private int    estoqueMinimo;  // Quantidade mínima desejada em estoque

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
     * Construtor legado (compatibilidade com código antigo).
     * Cria um produto sem estoque mínimo definido.
     *
     * @param id              : Identificador único
     * @param nome            : Nome do produto
     * @param categoria       : Categoria
     * @param precoUnitario   : Preço por unidade
     */
    public ProdutoNaoPerecivel(int id, String nome, String categoria, double precoUnitario) {
        this(id, nome, categoria, precoUnitario, 0);
    }

    // ═══════════════════════════════════════════════════════════════════
    // CÁLCULO DE PREJUÍZO
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Calcula o prejuízo causado pelo descarte deste produto.
     * O prejuízo é igual ao preço unitário (uma unidade perdida = um prejuízo de R$ preço).
     *
     * Uso prático:
     *  • Quando unidades são descartadas, prejuízo total = quantidade descartada × calcularPrejuizo()
     *  • Exemplo: 20 unidades de detergente a R$ 4,50 = prejuízo de R$ 90
     *
     * @return Prejuízo por unidade do produto (preço unitário)
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