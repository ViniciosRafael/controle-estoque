package com.mycompany.contole_estoque;

import java.time.LocalDate;

/**
 * Representa uma entrada de estoque vinculada a qualquer produto
 * (perecível ou não perecível).
 *
 * Para produtos perecíveis, os métodos isVencido() e diasParaVencer()
 * delegam para ProdutoPerecivel. Para não perecíveis, retornam valores
 * neutros (nunca vencido, Integer.MAX_VALUE dias).
 */
public class LoteEstoque {

    private int       idLote;
    private String    numeroLote;     // número/código do lote informado pelo usuário
    private Produto   produto;       // agora aceita qualquer Produto
    private int       quantidade;
    private LocalDate dataEntrada;

    public LoteEstoque() {}

    public LoteEstoque(int idLote, String numeroLote, Produto produto, int quantidade, LocalDate dataEntrada) {
        this.idLote      = idLote;
        this.numeroLote  = numeroLote;
        this.produto     = produto;
        this.quantidade  = quantidade;
        this.dataEntrada = dataEntrada;
    }

    /** true somente se o produto for perecível e já estiver vencido. */
    public boolean isVencido() {
        if (produto instanceof ProdutoPerecivel pp) return pp.isVencido();
        return false;
    }

    /**
     * Dias restantes até o vencimento.
     * Retorna Integer.MAX_VALUE para produtos não perecíveis.
     */
    public int diasParaVencer() {
        if (produto instanceof ProdutoPerecivel pp) return pp.diasParaVencer();
        return Integer.MAX_VALUE;
    }

    /** Reduz a quantidade do lote em n unidades. */
    public void darBaixa(int qtd) {
        if (qtd <= this.quantidade) {
            this.quantidade -= qtd;
            System.out.println("Baixa de " + qtd + " itens. Quantidade atual: " + this.quantidade);
        } else {
            System.out.println("Erro: quantidade informada excede o disponível no lote.");
        }
    }

    // ─── getters / setters ───────────────────────────────────────────────────
    public int       getIdLote()      { return idLote;      }
    public String    getNumeroLote()  { return numeroLote;  }
    public Produto   getProduto()     { return produto;     }
    public int       getQuantidade()  { return quantidade;  }
    public LocalDate getDataEntrada() { return dataEntrada; }

    public void setIdLote(int idLote)            { this.idLote      = idLote;      }
    public void setNumeroLote(String n)          { this.numeroLote  = n;           }
    public void setProduto(Produto produto)       { this.produto     = produto;     }
    public void setQuantidade(int quantidade)     { this.quantidade  = quantidade;  }
    public void setDataEntrada(LocalDate d)       { this.dataEntrada = d;           }
}
