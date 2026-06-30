package com.mycompany.contole_estoque;


/**
 * Classe base abstrata para todos os produtos do estoque.
 * Contém informações essenciais compartilhadas, como id, nome e categoria.
 */
public abstract class Produto {
    private int    id;        // ID único do produto
    private String nome;      // Nome do produto
    private String categoria; // Categoria/classificação

    /**
     * Construtor padrão vazio.
     */
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


    // --- Getters ---
    public String getNome()      { return this.nome;      }
    public int    getId()        { return this.id;        }
    public String getCategoria() { return this.categoria; }

    // --- Setters ---
    public void setNome(String nome)         { this.nome = nome;         }
    public void setId(int id)                { this.id   = id;           }
    public void setCategoria(String cat)     { this.categoria = cat;     }

    /**
     * Obtém o estoque mínimo (default: 0). Pode ser sobrescrito nas subclasses.
     * @return valor de estoque mínimo.
     */
    public int getEstoqueMinimo() { return 0; }

    /**
     * Calcula o prejuízo gerado pela perda de uma unidade do produto.
     * Método abstrato a ser implementado pelas subclasses.
     * @return valor do prejuízo financeiro.
     */
    public abstract double calcularPrejuizo();

    /**
     * Simula o registro deste produto exibindo log no console.
     */
    public void cadastrar() {
        System.out.println("Produto '" + this.nome + "' cadastrado com sucesso no sistema.");
    }
}