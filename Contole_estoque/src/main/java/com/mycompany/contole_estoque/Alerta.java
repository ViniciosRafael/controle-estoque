/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.contole_estoque;

/**
 *
 * @author vinic
 */
public abstract class Alerta {
    protected String mensagem;
    
    
    public Alerta() {}

    public Alerta(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public abstract void emitir();
    
    public String getMensagem() {return mensagem;}

    public void setMensagem(String mensagem) {this.mensagem = mensagem;}
}
