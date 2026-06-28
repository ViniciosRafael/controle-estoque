/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Produto perecível — indica que o produto pode vencer.
 * A data de validade específica é registrada em cada LoteEstoque,
 * pois lotes diferentes do mesmo produto podem ter validades diferentes.
 */
public class ProdutoPerecivel extends Produto {
    private int    estoqueMinimo;
    private double precoUnitario;

    public ProdutoPerecivel() { super(); }

    public ProdutoPerecivel(int id, String nome, String categoria, int estoqueMinimo, double precoUnitario) {
        super(id, nome, categoria);
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
    }

    public int    getEstoqueMinimo()  { return estoqueMinimo;  }
    public double getPrecoUnitario()  { return precoUnitario;  }

    public void setEstoqueMinimo(int estoqueMinimo)       { this.estoqueMinimo = estoqueMinimo; }
    public void setPrecoUnitario(double precoUnitario)    { this.precoUnitario = precoUnitario; }

    @Override
    public double calcularPrejuizo() {
        return this.precoUnitario;
    }
}