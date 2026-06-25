package com.mycompany.contole_estoque.store;

import com.mycompany.contole_estoque.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton que mantém todos os dados da aplicação em memória.
 * Pré-carregado com dados de exemplo ao inicializar.
 */
public class EstoqueStore {

    private static final EstoqueStore INSTANCE = new EstoqueStore();

    private final List<ProdutoPerecivel>    perec     = new ArrayList<>();
    private final List<ProdutoNaoPerecivel> naoPerec  = new ArrayList<>();
    private final List<LoteEstoque>         lotes     = new ArrayList<>();
    private final List<Descarte>            descartes = new ArrayList<>();
    private final List<Alerta>              alertas   = new ArrayList<>();
    private int nextId = 20;

    private EstoqueStore() {
        carregarDadosExemplo();
        gerarAlertas();
    }

    public static EstoqueStore get() { return INSTANCE; }

    // ------------------------------------------------------------------ dados
    private void carregarDadosExemplo() {
        ProdutoPerecivel leite   = new ProdutoPerecivel(1, "Leite Integral 1L",   "Laticínios", LocalDate.now().plusDays(7),  10, 4.50);
        ProdutoPerecivel queijo  = new ProdutoPerecivel(2, "Queijo Mussarela",    "Laticínios", LocalDate.now().plusDays(15),  5, 35.00);
        ProdutoPerecivel iogurte = new ProdutoPerecivel(3, "Iogurte Natural",     "Laticínios", LocalDate.now().plusDays(2),   8, 6.80);
        ProdutoPerecivel frango  = new ProdutoPerecivel(4, "Frango Resfriado 1kg","Carnes",     LocalDate.now().minusDays(1), 20, 18.90);
        ProdutoPerecivel presunto= new ProdutoPerecivel(5, "Presunto Fatiado",    "Frios",      LocalDate.now().plusDays(4),  10, 12.50);

        perec.add(leite);
        perec.add(queijo);
        perec.add(iogurte);
        perec.add(frango);
        perec.add(presunto);

        naoPerec.add(new ProdutoNaoPerecivel(6,  "Arroz 5kg",           "Cereais",     22.50));
        naoPerec.add(new ProdutoNaoPerecivel(7,  "Feijão Carioca 1kg",  "Leguminosas",  8.90));
        naoPerec.add(new ProdutoNaoPerecivel(8,  "Óleo de Soja 900ml",  "Óleos",        7.20));
        naoPerec.add(new ProdutoNaoPerecivel(9,  "Macarrão 500g",       "Massas",       4.30));
        naoPerec.add(new ProdutoNaoPerecivel(10, "Sal Refinado 1kg",    "Temperos",     2.50));

        lotes.add(new LoteEstoque(1, leite,    25, LocalDate.now().minusDays(3)));
        lotes.add(new LoteEstoque(2, queijo,   12, LocalDate.now().minusDays(5)));
        lotes.add(new LoteEstoque(3, iogurte,   6, LocalDate.now().minusDays(1)));
        lotes.add(new LoteEstoque(4, frango,   30, LocalDate.now().minusDays(10)));
        lotes.add(new LoteEstoque(5, presunto,  9, LocalDate.now().minusDays(2)));
    }

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
