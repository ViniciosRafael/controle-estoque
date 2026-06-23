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
public class LoteEstoque {
    private int idLote;
    private Produto produto; 
    private int quantidade;
    private LocalDate dataEntrada;
    
    public LoteEstoque(){}
    
    public LoteEstoque(int idLote, Produto produto, int quantidade, LocalDate dataEntrada){
        this.idLote = idLote;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataEntrada = dataEntrada;
    }
    
    public void darBaixa(int qtd) {
        if (qtd <= this.quantidade) {
            this.quantidade -= qtd;
            System.out.println("Baixa de " + qtd + " itens realizada. Quantidade atual: " + this.quantidade);
        } else {
            System.out.println("Erro: A quantidade informada excede o total disponível no lote.");
        }
    }

    public int GetIdLote(){return idLote;}
    public Produto GetProduto(){return produto;}
    public int GetQuantidade(){return quantidade;}
    public LocalDate getDataEntrada(){return dataEntrada;}
    
    public void setIdLote(int idLote){this.idLote = idLote;}
    public void setIProduto(Produto produto){this.produto = produto;}
    public void setIQuantidade(int quantidade){this.quantidade = quantidade;}
    public void setDataEntrada(LocalDate dataEntrada){this.dataEntrada = dataEntrada;}
}
