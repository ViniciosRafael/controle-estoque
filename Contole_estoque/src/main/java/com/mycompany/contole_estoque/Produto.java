package com.mycompany.contole_estoque;


public abstract class Produto {
    private int    id;        // ID único do produto
    private String nome;      // Nome do produto
    private String categoria; // Categoria/classificação

    public Produto() {}

    /**
     * Construtor completo do Produto.
     * @param id        : Identificador único
     * @param nome      : Nome descritivo
     * @param categoria : Classificação do produto
     */
    public Produto(int id, String nome, String categoria) {
        this.id        = id;
        this.nome      = nome;
        this.categoria = categoria;
    }


    public String getNome()      { return this.nome;      }
    public int    getId()        { return this.id;        }
    public String getCategoria() { return this.categoria; }


    public void setNome(String nome)         { this.nome = nome;         }
    public void setId(int id)                { this.id   = id;           }
    public void setCategoria(String cat)     { this.categoria = cat;     }


    public int getEstoqueMinimo() { return 0; }


    public abstract double calcularPrejuizo();

    public void cadastrar() {
        System.out.println("Produto '" + this.nome + "' cadastrado com sucesso no sistema.");
    }
}