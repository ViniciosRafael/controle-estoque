    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

/**
 *
 * @author vinic
 */
public abstract class Produto {
    private int id;
    private String nome;
    private String categoria;
    
    public Produto(){}
    
    public Produto(int id, String nome, String categoria){
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }
    
    public String getNome(){return this.nome;}
    public int getId(){return this.id;}
    public String getCategoria(){return this.categoria;}
    
    public void setNome(String nome){this.nome = nome;}
    public void setId(int id){this.id = id;}
    public void setCategoria(String categoria){this.categoria = categoria;}
    
    public abstract double calcularPrejuizo();
    
    public void cadastrar() {
      //implementação futura
        System.out.println("Produto '" + this.nome + "' cadastrado com sucesso no sistema.");
    }
}
