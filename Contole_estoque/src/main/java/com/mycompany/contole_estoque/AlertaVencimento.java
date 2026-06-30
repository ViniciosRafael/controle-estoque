package com.mycompany.contole_estoque;

/**
 * Alerta de vencimento de lote.
 * Cobre tanto lotes já vencidos quanto lotes próximos ao vencimento (<=5 dias).
 */
public class AlertaVencimento extends Alerta {
    private int diasRestantes;  // Armazena o número de dias até vencimento (negativo = vencido)

    // Construtor vazio - usado para instanciação genérica
    public AlertaVencimento() { super(); }

    // Construtor principal que cria alerta a partir de um lote
    public AlertaVencimento(int alertaId, LoteEstoque lote) {
        super(alertaId, buildMensagem(lote));  // Passa ID e mensagem para classe pai
        this.diasRestantes = lote.diasParaVencer();  // Calcula dias restantes do lote
    }

    // Método estático que constrói a mensagem apropriada (vencido ou próximo ao vencimento)
    private static String buildMensagem(LoteEstoque lote) {
        // Obtém nome do produto com fallback para "(produto removido)" se não encontrado
        String nomeProduto = lote.getProduto() != null ? lote.getProduto().getNome() : "(produto removido)";
        // Obtém número do lote ou converte ID para string como fallback
        String idLote = lote.getNumeroLote() != null ? lote.getNumeroLote() : String.valueOf(lote.getIdLote());
        
        // Verifica se lote já está vencido
        if (lote.isVencido()) {
            // Calcula quantos dias passaram desde o vencimento (valor absoluto)
            int diasVencido = Math.abs(lote.diasParaVencer());
            // Retorna mensagem alertando sobre vencimento já ocorrido
            return "VENCIDO — Lote '" + idLote + "' do produto '" + nomeProduto
                    + "' venceu há " + diasVencido + " dia(s). Descarte imediato recomendado.";
        } else {
            // Lote ainda não venceu - calcula dias restantes
            int dias = lote.diasParaVencer();
            // Retorna mensagem alertando sobre próximo vencimento
            return "Lote '" + idLote + "' do produto '" + nomeProduto
                    + "' vence em " + dias + " dia(s). Atenção!";
        }
    }

    // Implementação obrigatória: emite/exibe o alerta no console
    @Override
    public void emitir() {
        // Exibe tipo do alerta + mensagem completa
        System.out.println("[" + getTipo() + "] " + getMensagem());
        // Exibe dias restantes para referência rápida
        System.out.println("  Dias restantes: " + diasRestantes);
        // Exibe quando o alerta foi gerado
        System.out.println("  Data do alerta: " + getData());
    }

    // Implementação obrigatória: retorna o tipo identificador do alerta para filtros
    @Override
    public String getTipo() {
        return "VENCIMENTO";  // Tipo constante para classificação em painéis
    }

    // Getter: retorna número de dias restantes até vencimento
    public int getDiasRestantes(){ return diasRestantes; }
    // Setter: permite modificar o número de dias restantes armazenado
    public void setDiasRestantes(int diasRestantes){this.diasRestantes = diasRestantes;}
}
