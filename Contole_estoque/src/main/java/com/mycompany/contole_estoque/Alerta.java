package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 * Classe abstrata base para alertas do sistema de controle de estoque.
 */
public abstract class Alerta {
    protected int alertaId;
    protected String mensagem;
    protected LocalDate data;

    public Alerta() {}

    public Alerta(int alertaId, String mensagem) {
        this.alertaId = alertaId;
        this.mensagem = mensagem;
        this.data     = LocalDate.now();
    }

    public abstract void emitir();
    public abstract String getTipo();

    public int getAlertaId(){ return alertaId; }
    public String getMensagem(){ return mensagem; }
    public LocalDate getData(){ return data; }

    public void setAlertaId(int alertaId){ this.alertaId = alertaId; }
    public void setMensagem(String mensagem){ this.mensagem = mensagem; }
    public void setData(LocalDate data){this.data = data; }
}