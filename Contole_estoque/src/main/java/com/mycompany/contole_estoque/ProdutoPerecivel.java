/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author vinic
 */
public class ProdutoPerecivel extends Produto {
    private LocalDate dataValidade;
    private int estoqueMinimo;
    private double precoUnitario;
    
    public ProdutoPerecivel(){super();}
    
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
    
    public void setDataValidade(LocalDate dataValidade){this.dataValidade = dataValidade;}
    public void setEstoqueMinimo(int estoqueMinimo){this.estoqueMinimo = estoqueMinimo;}
    public void setPrecoUnitario(double precoUnitario) {this.precoUnitario = precoUnitario;}
    
    /**
     Verifica se a data de validade já passou em relação à data atual do sistema. treu p/ vencido false ainda não
     * @return 
     */
    public boolean isVencido() {
        return LocalDate.now().isAfter(this.dataValidade);
    }
    
    /**
     * Retorna a quantidade de dias restantes até o vencimento.
     * Se o produto já estiver vencido, o retorno será um número negativo.
     * @return int número de dias.
     */
    public int diasParaVencer() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), this.dataValidade);
    }
    
    @Override
    public double calcularPrejuizo() {
        return this.precoUnitario; 
    }
}
