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
    private ProdutoPerecivel produto;
    private int quantidade;
    private LocalDate dataEntrada;
    
    public LoteEstoque(){}
    
    public LoteEstoque(int idLote, ProdutoPerecivel produto, int quantidade, LocalDate dataEntrada){
        this.idLote = idLote;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataEntrada = dataEntrada;
    }
    
    /** Delega para ProdutoPerecivel: true se a data de validade já passou.
     * @return  */
    public boolean isVencido()   { return produto.isVencido(); }
    
    /** Delega para ProdutoPerecivel: dias restantes até o vencimento (negativo se já vencido).
     * @return  */
    public int diasParaVencer() {
        return produto.diasParaVencer();
    }
    
    /** Reduz a quantidade do lote em n unidades.
     * @param qtd */
    public void darBaixa(int qtd) {
        if (qtd <= this.quantidade) {
            this.quantidade -= qtd;
            System.out.println("Baixa de " + qtd + " itens realizada. Quantidade atual: " + this.quantidade);
        } else {
            System.out.println("Erro: A quantidade informada excede o total disponível no lote.");
        }
    }

    public int getIdLote(){return idLote;}
    public ProdutoPerecivel  getProduto(){return produto;}
    public int getQuantidade(){return quantidade;}
    public LocalDate getDataEntrada(){return dataEntrada;}
    
    public void setIdLote(int idLote){this.idLote = idLote;}
    public void setProduto(ProdutoPerecivel  produto){this.produto = produto;}
    public void setIQuantidade(int quantidade){this.quantidade = quantidade;}
    public void setDataEntrada(LocalDate dataEntrada){this.dataEntrada = dataEntrada;}

}
