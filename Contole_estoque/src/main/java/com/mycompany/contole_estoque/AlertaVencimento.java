package com.mycompany.contole_estoque;

/**
 * Alerta de vencimento de lote.
 * Cobre tanto lotes já vencidos quanto lotes próximos ao vencimento (<=5 dias).
 */
public class AlertaVencimento extends Alerta {
    private int diasRestantes;

    public AlertaVencimento() { super(); }

    public AlertaVencimento(int alertaId, LoteEstoque lote) {
        super(alertaId, buildMensagem(lote));
        this.diasRestantes = lote.diasParaVencer();
    }

    private static String buildMensagem(LoteEstoque lote) {
        String nomeProduto = lote.getProduto() != null ? lote.getProduto().getNome() : "(produto removido)";
        String idLote = lote.getNumeroLote() != null ? lote.getNumeroLote() : String.valueOf(lote.getIdLote());
        if (lote.isVencido()) {
            int diasVencido = Math.abs(lote.diasParaVencer());
            return "VENCIDO — Lote '" + idLote + "' do produto '" + nomeProduto
                    + "' venceu há " + diasVencido + " dia(s). Descarte imediato recomendado.";
        } else {
            int dias = lote.diasParaVencer();
            return "Lote '" + idLote + "' do produto '" + nomeProduto
                    + "' vence em " + dias + " dia(s). Atenção!";
        }
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
