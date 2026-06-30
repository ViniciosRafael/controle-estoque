package com.mycompany.contole_estoque;

/**
 * Alerta de estoque mínimo atingido.
 */
public class AlertaEstoqueMinimo extends Alerta {
    private int qtdAtual;

    public AlertaEstoqueMinimo() { super(); }

    public AlertaEstoqueMinimo(int alertaId, LoteEstoque lote) {
        this(alertaId, lote, lote.getQuantidade());
    }

    public AlertaEstoqueMinimo(int alertaId, LoteEstoque lote, int qtdTotal) {
        super(alertaId, buildMensagem(lote, qtdTotal));
        this.qtdAtual = qtdTotal;
    }

    private static String buildMensagem(LoteEstoque lote, int qtdTotal) {
        String idLote = lote.getNumeroLote() != null ? lote.getNumeroLote() : String.valueOf(lote.getIdLote());
        return "ESTOQUE BAIXO — O produto '" + lote.getProduto().getNome() 
                + "' (Lote: " + idLote + ") atingiu o nível crítico."
                + " Total em estoque: " + qtdTotal 
                + " | Mínimo: " + lote.getProduto().getEstoqueMinimo();
    }

    @Override
    public void emitir() {
        System.out.println("[" + getTipo() + "] " + getMensagem());
        System.out.println("  Quantidade total: " + qtdAtual);
        System.out.println("  Data do alerta:   " + getData());
    }

    @Override
    public String getTipo() {
        return "ESTOQUE_MINIMO";
    }

    public int getQtdAtual(){ return qtdAtual; }
    public void setQtdAtual(int qtdAtual){this.qtdAtual = qtdAtual;}
}