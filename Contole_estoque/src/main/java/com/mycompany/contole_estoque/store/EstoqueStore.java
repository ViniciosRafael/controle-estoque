package com.mycompany.contole_estoque.store;

import com.mycompany.contole_estoque.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe é o "cérebro" e a "memória" do sistema.
 * Ela guarda todas as listas (produtos, lotes, alertas, etc.) em um só lugar.
 * Usa o padrão "Singleton", o que significa que existe apenas UMA cópia dela rodando na memória.
 */
public class EstoqueStore {

    // A única instância da memória do sistema
    private static final EstoqueStore INSTANCE = new EstoqueStore();

    // Listas que guardam os dados do sistema
    private final List<ProdutoPerecivel>    perec         = new ArrayList<>(); // Produtos que vencem
    private final List<ProdutoNaoPerecivel> naoPerec      = new ArrayList<>(); // Produtos que não vencem
    private final List<LoteEstoque>         lotes         = new ArrayList<>(); // Todas as entradas de estoque
    private final List<Descarte>            descartes     = new ArrayList<>(); // Registro de produtos jogados fora
    private final List<Alerta>              alertas       = new ArrayList<>(); // Avisos de validade ou falta de estoque
    private final List<Movimentacao>        movimentacoes = new ArrayList<>(); // Histórico de tudo que foi feito
    
    private int nextId = 1; // Contador para criar IDs únicos automaticamente

    // Construtor privado: impede que outras partes do código criem "outras memórias"
    private EstoqueStore() {
    }

    // Método para acessar a memória do sistema de qualquer lugar
    public static EstoqueStore get() { return INSTANCE; }

    // Gera o próximo número de ID disponível
    public int nextId() { return nextId++; }

    /**
     * Este método varre todo o estoque e cria avisos automáticos se:
     * 1. O produto estiver vencido ou quase vencendo (menos de 5 dias).
     * 2. A quantidade do lote for menor que o estoque mínimo definido para o produto.
     */
    public void gerarAlertas() {
        alertas.clear(); // Limpa os avisos antigos antes de criar novos
        int id = 1;
        for (LoteEstoque lote : lotes) {
            Produto prod = lote.getProduto();
            if (prod == null) continue;

            // 1. Alerta de vencimento:
            // Cria aviso se o produto tiver validade e estiver vencido OU faltar 5 dias ou menos.
            if (lote.getDataValidade() != null) {
                int dias = lote.diasParaVencer();
                if (lote.isVencido() || dias <= 5) {
                    alertas.add(new AlertaVencimento(id++, lote));
                }
            }

            // 2. Alerta de estoque mínimo:
            // Cria aviso se ainda houver produto no lote, mas a quantidade for menor que o mínimo.
            if (lote.getQuantidade() > 0
                    && prod.getEstoqueMinimo() > 0
                    && lote.getQuantidade() < prod.getEstoqueMinimo()) {
                alertas.add(new AlertaEstoqueMinimo(id++, lote));
            }
        }
    }

    // Métodos para pegar as listas de dados do sistema
    public List<ProdutoPerecivel>    getPerec()          { return perec;         }
    public List<ProdutoNaoPerecivel> getNaoPerec()       { return naoPerec;      }
    public List<LoteEstoque>         getLotes()          { return lotes;         }
    public List<Descarte>            getDescartes()      { return descartes;     }
    public List<Alerta>              getAlertas()        { return alertas;       }
    public List<Movimentacao>        getMovimentacoes()  { return movimentacoes; }
}
