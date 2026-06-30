package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 * Classe abstrata base para alertas do sistema de controle de estoque.
 */
public abstract class Alerta {
    protected int alertaId;      // ID único deste alerta para referência
    protected String mensagem;   // Mensagem descritiva do alerta
    protected LocalDate data;    // Data em que o alerta foi gerado

    // Construtor vazio - necessário para subclasses que precisam instanciar sem parâmetros
    public Alerta() {}

    // Construtor principal que inicializa os dados do alerta
    public Alerta(int alertaId, String mensagem) {
        this.alertaId = alertaId;          // Atribui ID único
        this.mensagem = mensagem;          // Atribui mensagem descritiva
        this.data     = LocalDate.now();   // Registra automaticamente a data atual
    }

    // Método abstrato que cada subclasse deve implementar para exibir o alerta
    // Cada tipo de alerta define seu próprio meio de emissão (console, UI, etc)
    public abstract void emitir();
    
    // Método abstrato que cada subclasse deve implementar para retornar seu tipo
    // Usado para classificar e filtrar alertas em painéis
    public abstract String getTipo();

    /** @return O ID único deste alerta */
    public int getAlertaId(){ return alertaId; } 
    /** @return A mensagem descritiva do alerta */
    public String getMensagem(){ return mensagem; }
    /** @return A data de geração do alerta */
    public LocalDate getData(){ return data; }

    /** Modifica o ID do alerta */
    public void setAlertaId(int alertaId){ this.alertaId = alertaId; }
    
    /** Modifica a mensagem descritiva do alerta */
    public void setMensagem(String mensagem){ this.mensagem = mensagem; }
    
    /** Modifica a data de geração do alerta */
    public void setData(LocalDate data){this.data = data; }
}