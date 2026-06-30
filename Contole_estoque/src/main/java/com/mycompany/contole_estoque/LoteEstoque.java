package com.mycompany.contole_estoque;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class LoteEstoque {

    private int       idLote;        // ID único deste lote
    private String    numeroLote;    // Código/número do lote informado pelo usuário
    private Produto   produto;       // Produto deste lote
    private int       quantidade;    // Quantidade atual disponível
    private LocalDate dataEntrada;   // Data de entrada no estoque
    private LocalDate dataValidade;  // Data de vencimento (NULL para não perecíveis)

    public LoteEstoque() {}

    /**
     * Construtor completo do LoteEstoque.
     *
     * @param idLote       : ID único deste lote
     * @param numeroLote   : Código do lote (ex: "LOTE-001", "LT-2024-001")
     * @param produto      : Produto deste lote
     * @param quantidade   : Quantidade de unidades
     * @param dataEntrada  : Data de entrada no estoque
     * @param dataValidade : Data de validade (NULL se não perecível)
     */
    public LoteEstoque(int idLote, String numeroLote, Produto produto, int quantidade,LocalDate dataEntrada, LocalDate dataValidade) {
        this.idLote       = idLote;
        this.numeroLote   = numeroLote;
        this.produto      = produto;
        this.quantidade   = quantidade;
        this.dataEntrada  = dataEntrada;
        this.dataValidade = dataValidade;
    }


    /**
     * Verifica se este lote já está vencido.
     * Retorna FALSE para lotes de produtos não perecíveis (dataValidade == NULL).
     *
     * @return true se o lote está vencido, false caso contrário
     */
    public boolean isVencido() {
        if (dataValidade == null) return false; // Não perecíveis nunca vencem
        return LocalDate.now().isAfter(dataValidade);
    }

    
    public int diasParaVencer() {
        if (dataValidade == null) return Integer.MAX_VALUE; // Sem prazo de validade
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
    }


    public void darBaixa(int qtd) {
        // Validação 1: Quantidade deve ser positiva
        if (qtd <= 0) {
            System.out.println("Erro: quantidade para baixa deve ser maior que zero.");
            return;
        }
        // Validação 2: Quantidade não pode exceder o disponível
        if (qtd <= this.quantidade) {
            this.quantidade -= qtd;
            System.out.println("Baixa de " + qtd + " itens. Quantidade atual: " + this.quantidade);
        } else {
            System.out.println("Erro: quantidade informada excede o disponível no lote.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // GETTERS E SETTERS
    // ═══════════════════════════════════════════════════════════════════
    public int       getIdLote()       { return idLote;       }
    public String    getNumeroLote()   { return numeroLote;   }
    public Produto   getProduto()      { return produto;      }
    public int       getQuantidade()   { return quantidade;   }
    public LocalDate getDataEntrada()  { return dataEntrada;  }
    public LocalDate getDataValidade() { return dataValidade; }

    public void setIdLote(int idLote)              { this.idLote       = idLote;       }
    public void setNumeroLote(String n)            { this.numeroLote   = n;            }
    public void setProduto(Produto produto)        { this.produto      = produto;      }
    public void setQuantidade(int quantidade)      { this.quantidade   = quantidade;   }
    public void setDataEntrada(LocalDate d)        { this.dataEntrada  = d;            }
    public void setDataValidade(LocalDate d)       { this.dataValidade = d;            }
}