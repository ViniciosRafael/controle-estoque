/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

/**
 *
 * @author vinic
 */
public class AlertaEstoqueMinimo {
    private int qtdAtual;

    public AlertaEstoqueMinimo() { super(); }

    public AlertaEstoqueMinimo(int alertaId, LoteEstoque lote) {
        super(alertaId,
              "O lote #" + lote.getIdLote()
              + " do produto '" + lote.getProduto().getNome()
              + "' está abaixo do estoque mínimo."
              + " Atual: " + lote.getQuantidade()
              + " | Mínimo: " + lote.getProduto().getEstoqueMinimo());
        this.qtdAtual = lote.getQuantidade();
    }

    @Override
    public void emitir() {
        System.out.println("[" + getTipo() + "] " + getMensagem());
        System.out.println("  Quantidade atual: " + qtdAtual);
        System.out.println("  Data do alerta:   " + getData());
    }

    @Override
    public String getTipo() {
        return "ESTOQUE_MINIMO";
    }

    public int getQtdAtual(){ return qtdAtual; }
    public void setQtdAtual(int qtdAtual){this.qtdAtual = qtdAtual;}
}
