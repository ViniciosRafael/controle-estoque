package com.mycompany.contole_estoque.store;

import com.mycompany.contole_estoque.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton que mantém todos os dados da aplicação em memória.
 * As listas iniciam vazias; dados são adicionados pelo usuário.
 */
public class EstoqueStore {

    private static final EstoqueStore INSTANCE = new EstoqueStore();

    private final List<ProdutoPerecivel>    perec     = new ArrayList<>();
    private final List<ProdutoNaoPerecivel> naoPerec  = new ArrayList<>();
    private final List<LoteEstoque>         lotes     = new ArrayList<>();
    private final List<Descarte>            descartes = new ArrayList<>();
    private final List<Alerta>              alertas   = new ArrayList<>();
    private int nextId = 1;

    private EstoqueStore() {
        // listas iniciam vazias
    }

    public static EstoqueStore get() { return INSTANCE; }


    // ---------------------------------------------------------------- helpers
    public int nextId() { return nextId++; }

    /** Recalcula todos os alertas com base nos lotes atuais. */
    public void gerarAlertas() {
        alertas.clear();
        int id = 1;
        for (LoteEstoque lote : lotes) {
            if (lote.getQuantidade() <= 0) continue;

            int dias = lote.diasParaVencer();
            if (lote.isVencido() || dias <= 5) {
                alertas.add(new AlertaVencimento(id++, lote));
            }
            if (lote.getQuantidade() < lote.getProduto().getEstoqueMinimo()) {
                alertas.add(new AlertaEstoqueMinimo(id++, lote));
            }
        }
    }

    // --------------------------------------------------------------- acessors
    public List<ProdutoPerecivel>    getPerec()     { return perec;     }
    public List<ProdutoNaoPerecivel> getNaoPerec()  { return naoPerec;  }
    public List<LoteEstoque>         getLotes()     { return lotes;     }
    public List<Descarte>            getDescartes() { return descartes; }
    public List<Alerta>              getAlertas()   { return alertas;   }
}
