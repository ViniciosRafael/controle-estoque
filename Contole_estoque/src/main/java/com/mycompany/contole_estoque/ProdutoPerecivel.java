package com.mycompany.contole_estoque;

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

    @Override
    public int getEstoqueMinimo()  { return estoqueMinimo;  }

  
    public double getPrecoUnitario()  { return precoUnitario;  }

 
    public void setEstoqueMinimo(int estoqueMinimo)       { this.estoqueMinimo = estoqueMinimo; }


    public void setPrecoUnitario(double precoUnitario)    { this.precoUnitario = precoUnitario; }

 
    @Override
    public double calcularPrejuizo() {
        return this.precoUnitario;
    }
}