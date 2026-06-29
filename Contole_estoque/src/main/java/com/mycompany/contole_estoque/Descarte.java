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
public class Descarte {
    private int descarteId;
    private LoteEstoque lote; // Associação com o lote que originou a perda
    private int quantidadeDescartada;
    private LocalDate dataDescarte;
    private String motivo; // Ex: "Prazo de validade expirado", "Avaria física"

    // Construtor vazio
    public Descarte() {}

    // Construtor completo
    public Descarte(int descarteId, LoteEstoque lote, int quantidadeDescartada, LocalDate dataDescarte, String motivo) {
        this.descarteId = descarteId;
        this.lote = lote;
        this.quantidadeDescartada = quantidadeDescartada;
        this.dataDescarte = dataDescarte;
        this.motivo = motivo;
    }

    public double calcularPrejuizo() {
        Produto produto = this.lote.getProduto();
        if (produto == null) return 0.0; // lote órfão: sem produto não há como calcular o prejuízo unitário
        double prejuizoUnitario = produto.calcularPrejuizo();
        return prejuizoUnitario * this.quantidadeDescartada;
    }


    public void registrar() {
        this.lote.darBaixa(this.quantidadeDescartada);

        Produto produto = this.lote.getProduto();
        System.out.println("REGISTO DE DESCARTE");
        System.out.println("Motivo: " + this.motivo);
        System.out.println("Produto: " + (produto != null ? produto.getNome() : "(produto removido)"));
        System.out.println("Quantidade: " + this.quantidadeDescartada);
        System.out.println("Prejuízo Total: " + this.calcularPrejuizo());
    }
    
    public int getDescarteId(){return descarteId;}
    public LoteEstoque getLote(){ return lote; }
    public int getQuantidadeDescartada() { return quantidadeDescartada; }
    public LocalDate getDataDescarte(){ return dataDescarte; }
    public String getMotivo(){ return motivo;}

    public void setDescarteId(int descarteId){ this.descarteId= descarteId; }
    public void setLote(LoteEstoque lote){ this.lote= lote; }
    public void setQuantidadeDescartada(int quantidadeDescartada){ this.quantidadeDescartada = quantidadeDescartada; }
    public void setDataDescarte(LocalDate dataDescarte){ this.dataDescarte = dataDescarte; }
    public void setMotivo(String motivo){ this.motivo= motivo; }
}