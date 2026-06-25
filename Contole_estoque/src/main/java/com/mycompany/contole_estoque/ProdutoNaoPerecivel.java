package com.mycompany.contole_estoque;

/**
 * Produto não perecível: possui preço unitário e estoque mínimo,
 * mas não tem data de validade.
 */
public class ProdutoNaoPerecivel extends Produto {

    private double precoUnitario;
    private int    estoqueMinimo;

    public ProdutoNaoPerecivel() { super(); }

    public ProdutoNaoPerecivel(int id, String nome, String categoria,
                               double precoUnitario, int estoqueMinimo) {
        super(id, nome, categoria);
        this.precoUnitario = precoUnitario;
        this.estoqueMinimo = estoqueMinimo;
    }

    /** Construtor legado (sem estoque mínimo) — mantido para compatibilidade. */
    public ProdutoNaoPerecivel(int id, String nome, String categoria, double precoUnitario) {
        this(id, nome, categoria, precoUnitario, 0);
    }

    @Override
    public double calcularPrejuizo() { return precoUnitario; }

    @Override
    public String getNome() { return super.getNome(); }

    @Override
    public void cadastrar() {
        System.out.println("Produto não perecível '" + getNome()
            + "' (categoria: " + getCategoria()
            + ") cadastrado. Preço unitário: R$ " + precoUnitario
            + " | Est. Mínimo: " + estoqueMinimo);
    }

    public double getPrecoUnitario()                    { return precoUnitario; }
    public void   setPrecoUnitario(double precoUnitario){ this.precoUnitario = precoUnitario; }

    public int  getEstoqueMinimo()               { return estoqueMinimo; }
    public void setEstoqueMinimo(int estoqueMinimo){ this.estoqueMinimo = estoqueMinimo; }
}
