/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

/**
 *
 * @author vinic
 */
public class ProdutoNaoPerecivel extends Produto {
    private double precoUnitario;

    public ProdutoNaoPerecivel(){super();}

    public ProdutoNaoPerecivel(int id, String nome, String categoria, double precoUnitario) {
        super(id, nome, categoria);
        this.precoUnitario = precoUnitario;
    }

    @Override
    public double calcularPrejuizo() {return precoUnitario;}

    @Override
    public String getNome() {return super.getNome();}

    @Override
    public void cadastrar() {
        System.out.println("Produto não perecível '" + getNome()+ "' (categoria: " + getCategoria()+ ") cadastrado com sucesso. Preço unitário: R$ " + precoUnitario);
    }

    public double getPrecoUnitario(){ return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario){ this.precoUnitario = precoUnitario; }
}
