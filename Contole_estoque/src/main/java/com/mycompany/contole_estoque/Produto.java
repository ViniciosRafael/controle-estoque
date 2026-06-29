    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

/**
 * Classe base para todos os produtos do estoque.
 * Contém os atributos comuns a perecíveis e não perecíveis.
 */
public abstract class Produto {
    private int    id;
    private String nome;
    private String categoria;

    public Produto() {}

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

    /**
     * Retorna o estoque mínimo configurado para o produto.
     * Subclasses que controlam estoque mínimo devem sobrescrever.
     * @return 
     */
    public int getEstoqueMinimo() { return 0; }

    public abstract double calcularPrejuizo();

    public void cadastrar() {
        System.out.println("Produto '" + this.nome + "' cadastrado com sucesso no sistema.");
    }
}