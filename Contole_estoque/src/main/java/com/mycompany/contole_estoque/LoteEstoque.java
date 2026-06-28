package com.mycompany.contole_estoque;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa uma entrada de estoque vinculada a qualquer produto
 * (perecível ou não perecível).
 *
 * Para produtos perecíveis, dataValidade armazena a validade deste lote
 * específico. Métodos isVencido() e diasParaVencer() usam este campo.
 * Para não perecíveis, dataValidade é null e esses métodos retornam
 * valores neutros.
 */
public class LoteEstoque {

    private int       idLote;
    private String    numeroLote;     // número/código do lote informado pelo usuário
    private Produto   produto;
    private int       quantidade;
    private LocalDate dataEntrada;
    private LocalDate dataValidade;   // null para não perecíveis

    public LoteEstoque() {}

    public LoteEstoque(int idLote, String numeroLote, Produto produto, int quantidade,
                       LocalDate dataEntrada, LocalDate dataValidade) {
        this.idLote       = idLote;
        this.numeroLote   = numeroLote;
        this.produto      = produto;
        this.quantidade   = quantidade;
        this.dataEntrada  = dataEntrada;
        this.dataValidade = dataValidade;
    }

    /** true somente se o produto for perecível e o lote já estiver vencido. */
    public boolean isVencido() {
        if (dataValidade == null) return false;
        return LocalDate.now().isAfter(dataValidade);
    }

    /**
     * Dias restantes até o vencimento deste lote.
     * Retorna Integer.MAX_VALUE para lotes sem data de validade (não perecíveis).
     */
    public int diasParaVencer() {
        if (dataValidade == null) return Integer.MAX_VALUE;
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
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
    public int       getIdLote()       { return idLote;       }
    public String    getNumeroLote()   { return numeroLote;   }
    public Produto   getProduto()      { return produto;      }
    public int       getQuantidade()   { return quantidade;   }
    public LocalDate getDataEntrada()  { return dataEntrada;  }
    public LocalDate getDataValidade() { return dataValidade; }

    public void setIdLote(int idLote)              { this.idLote       = idLote;       }
    public void setNumeroLote(String n)            { this.numeroLote   = n;            }
    public void setProduto(Produto produto)         { this.produto      = produto;      }
    public void setQuantidade(int quantidade)       { this.quantidade   = quantidade;   }
    public void setDataEntrada(LocalDate d)         { this.dataEntrada  = d;            }
    public void setDataValidade(LocalDate d)        { this.dataValidade = d;            }
}