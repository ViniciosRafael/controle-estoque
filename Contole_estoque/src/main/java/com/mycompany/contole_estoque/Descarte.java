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
        double prejuizoUnitario = this.lote.getProduto().calcularPrejuizo();
        return prejuizoUnitario * this.quantidadeDescartada;
    }


    public void registrar() {
        this.lote.darBaixa(this.quantidadeDescartada);
        
        System.out.println("REGISTO DE DESCARTE");
        System.out.println("Motivo: " + this.motivo);
        System.out.println("Produto: " + this.lote.getProduto().getNome());
        System.out.println("Quantidade: " + this.quantidadeDescartada);
        System.out.println("Prejuízo Total: € " + this.calcularPrejuizo());
    }

}
