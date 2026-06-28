/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

/**
 *
 * @author vinic
 */
public class AlertaVencimento extends Alerta {
    private int diasRestantes;

    public AlertaVencimento() { super(); }

    public AlertaVencimento(int alertaId, LoteEstoque lote) {
        super(alertaId,
              "O lote #" + lote.getIdLote()
              + " do produto '" + lote.getProduto().getNome()
              + "' vence em " + lote.diasParaVencer() + " dia(s).");
        this.diasRestantes = lote.diasParaVencer();
    }

    @Override
    public void emitir() {
        System.out.println("[" + getTipo() + "] " + getMensagem());
        System.out.println("  Dias restantes: " + diasRestantes);
        System.out.println("  Data do alerta: " + getData());
    }

    @Override
    public String getTipo() {
        return "VENCIMENTO";
    }

    public int getDiasRestantes(){ return diasRestantes; }
    public void setDiasRestantes(int diasRestantes){this.diasRestantes = diasRestantes;}
}