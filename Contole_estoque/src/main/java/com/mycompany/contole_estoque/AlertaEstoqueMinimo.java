package com.mycompany.contole_estoque;

/**
 * Alerta de estoque mínimo atingido.
 */
public class AlertaEstoqueMinimo extends Alerta {
    private int qtdAtual;  // Armazena a quantidade total em estoque

    // Construtor vazio - usado para instanciação genérica
    public AlertaEstoqueMinimo() { super(); }

    // Construtor conveniente: assume que qtdTotal é igual à quantidade do lote
    public AlertaEstoqueMinimo(int alertaId, LoteEstoque lote) {
        this(alertaId, lote, lote.getQuantidade());  // Chama construtor completo com qtd do lote
    }

    // Construtor completo que permite especificar a quantidade total customizada
    public AlertaEstoqueMinimo(int alertaId, LoteEstoque lote, int qtdTotal) {
        super(alertaId, buildMensagem(lote, qtdTotal));  // Passa ID e mensagem para classe pai
        this.qtdAtual = qtdTotal;  // Armazena quantidade total para referência
    }

    // Método estático que monta a mensagem formatada do alerta
    private static String buildMensagem(LoteEstoque lote, int qtdTotal) {
        // Obtém o número do lote (fallback para ID se número não estiver definido)
        String idLote = lote.getNumeroLote() != null ? lote.getNumeroLote() : String.valueOf(lote.getIdLote());
        // Monta a mensagem descritiva com nome do produto, lote, quantidade e mínimo
        return "ESTOQUE BAIXO — O produto '" + lote.getProduto().getNome() 
                + "' (Lote: " + idLote + ") atingiu o nível crítico."
                + " Total em estoque: " + qtdTotal 
                + " | Mínimo: " + lote.getProduto().getEstoqueMinimo();
    }

    // Implementação obrigatória: emite/exibe o alerta no console
    @Override
    public void emitir() {
        // Exibe tipo do alerta + mensagem completa
        System.out.println("[" + getTipo() + "] " + getMensagem());
        // Exibe quantidade atual em estoque para referência
        System.out.println("  Quantidade total: " + qtdAtual);
        // Exibe data de geração do alerta
        System.out.println("  Data do alerta:   " + getData());
    }

    // Implementação obrigatória: retorna o tipo identificador do alerta para filtros
    @Override
    public String getTipo() {
        return "ESTOQUE_MINIMO";  // Tipo constante para classificação em painéis
    }

    // Getter: retorna a quantidade atual armazenada
    public int getQtdAtual(){ return qtdAtual; }
    // Setter: permite modificar a quantidade armazenada
    public void setQtdAtual(int qtdAtual){this.qtdAtual = qtdAtual;}
}