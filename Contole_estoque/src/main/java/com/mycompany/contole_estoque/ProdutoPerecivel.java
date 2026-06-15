/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 *
 * @author vinic
 */
public class ProdutoPerecivel extends Produto {
    private LocalDate dataValidade;
    private int estoqueMinimo;
    private double precoUnitario;
    
    public ProdutoPerecivel(){}
    
    // Construtor completo utilizando herança
    public ProdutoPerecivel(int id, String nome, String categoria, LocalDate dataValidade, int estoqueMinimo, double precoUnitario) {
        super(id, nome, categoria); // Invoca o construtor da classe mãe Produto
        this.dataValidade = dataValidade;
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
    }
    
    public LocalDate getDataValidade(){return this.dataValidade;}
    public int getEstoqueMinimo(){return this.estoqueMinimo;}
    public double getPrecoUnitario(){return this.precoUnitario;}
    
    public void setDataValidade(){this.dataValidade = dataValidade;}
    public void setEstoqueMinimo(){this.estoqueMinimo = estoqueMinimo;}
    public void setPrecoUnitario() {this.precoUnitario = precoUnitario;}
    
}
