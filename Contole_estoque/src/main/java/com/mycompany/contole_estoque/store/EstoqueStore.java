package com.mycompany.contole_estoque.store;

import com.mycompany.contole_estoque.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * Remove todos os lotes que possuem quantidade zero da lista ativa de estoque.
     */
    public void limparLotesZerados() {
        lotes.removeIf(lote -> lote.getQuantidade() <= 0);
    }

    /**
     * Este método varre todo o estoque e cria avisos automáticos se:
     * 1. O produto estiver vencido ou quase vencendo (menos de 5 dias).
     * 2. O TOTAL de estoque do produto (soma de todos os lotes) for menor que o mínimo definido.
     * Os alertas são gerados de forma MISTURADA conforme os lotes são processados.
     */
    public void gerarAlertas() {
        // Antes de gerar alertas, removemos lotes que não existem mais fisicamente
        limparLotesZerados();
        
        alertas.clear(); // Limpa os avisos antigos antes de criar novos
        int idAlerta = 1;
        
        // Passo 1: Calcular o TOTAL de estoque por produto primeiro (para o alerta de estoque mínimo)
        Map<Integer, Integer> totalPorProduto = new HashMap<>();
        for (LoteEstoque lote : lotes) {
            Produto prod = lote.getProduto();
            if (prod != null && lote.getQuantidade() > 0) {
                int prodId = prod.getId();
                totalPorProduto.put(prodId, totalPorProduto.getOrDefault(prodId, 0) + lote.getQuantidade());
            }
        }

        // Passo 2: Iterar pelos lotes e gerar os alertas de forma MISTURADA
        Set<Integer> produtosComAlertaEstoque = new HashSet<>();
        
        for (LoteEstoque lote : lotes) {
            Produto prod = lote.getProduto();
            if (prod == null || lote.getQuantidade() <= 0) continue;

            // --- Tenta gerar Alerta de VENCIMENTO para este lote ---
            if (lote.getDataValidade() != null) {
                int dias = lote.diasParaVencer();
                if (lote.isVencido() || dias <= 5) {
                    alertas.add(new AlertaVencimento(idAlerta++, lote));
                }
            }

            // --- Tenta gerar Alerta de ESTOQUE MÍNIMO para o produto deste lote ---
            if (!produtosComAlertaEstoque.contains(prod.getId())) {
                int totalEstoque = totalPorProduto.getOrDefault(prod.getId(), 0);
                if (prod.getEstoqueMinimo() > 0 && totalEstoque > 0 && totalEstoque < prod.getEstoqueMinimo()) {
                    alertas.add(new AlertaEstoqueMinimo(idAlerta++, lote, totalEstoque));
                    produtosComAlertaEstoque.add(prod.getId());
                }
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