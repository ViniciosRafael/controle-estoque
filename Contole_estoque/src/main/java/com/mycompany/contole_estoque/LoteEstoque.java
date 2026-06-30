package com.mycompany.contole_estoque;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                     CLASSE: LOTE DE ESTOQUE                               ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Representa uma entrada/lote de um produto no estoque.
 * Cada lote é uma quantidade específica de um produto que entrou no estoque
 * em uma determinada data, podendo ter uma data de validade (se perecível).
 *
 * Exemplos práticos:
 *  • Lote 1: 50 unidades de Leite Integral, entrada 01/01/2024, vence 15/02/2024
 *  • Lote 2: 100 unidades de Papel Higiênico, entrada 05/01/2024, sem data de vencimento
 *
 * Atributos:
 *  • idLote       : ID único deste lote no sistema
 *  • numeroLote   : Código do lote informado pelo usuário/fornecedor (ex: "LOTE001")
 *  • produto      : Referência para o Produto associado
 *  • quantidade   : Quantidade atual disponível do lote
 *  • dataEntrada  : Data em que o lote entrou no estoque
 *  • dataValidade : Data de vencimento (NULL para não perecíveis)
 *
 * Responsabilidades:
 *  • Gerenciar a quantidade disponível (dar baixa quando produtos são retirados)
 *  • Detectar vencimento de produtos perecíveis
 *  • Calcular dias restantes até vencimento
 *  • Manter rastreabilidade através do número de lote
 */
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
    public LoteEstoque(int idLote, String numeroLote, Produto produto, int quantidade,
                       LocalDate dataEntrada, LocalDate dataValidade) {
        this.idLote       = idLote;
        this.numeroLote   = numeroLote;
        this.produto      = produto;
        this.quantidade   = quantidade;
        this.dataEntrada  = dataEntrada;
        this.dataValidade = dataValidade;
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE VERIFICAÇÃO DE VALIDADE
    // ═══════════════════════════════════════════════════════════════════

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

    /**
     * Calcula quantos dias faltam até o vencimento deste lote.
     * Para lotes sem data de validade (não perecíveis), retorna Integer.MAX_VALUE.
     * Se já está vencido, retorna um número negativo.
     *
     * Exemplos:
     *  • Lote vence em 5 dias  → retorna 5
     *  • Lote já venceu há 3 dias → retorna -3
     *  • Produto não perecível → retorna Integer.MAX_VALUE
     *
     * @return Número de dias até vencimento (ou negativo se vencido)
     */
    public int diasParaVencer() {
        if (dataValidade == null) return Integer.MAX_VALUE; // Sem prazo de validade
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE MANIPULAÇÃO DE QUANTIDADE
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Processa uma retirada (baixa) de quantidade deste lote.
     * Este método é usado quando produtos são vendidos, descartados ou transferidos.
     *
     * VALIDAÇÕES:
     *  ✓ Quantidade deve ser positiva (> 0)
     *  ✓ Quantidade não pode exceder o disponível
     *  ✓ Se validação falhar, a quantidade não é alterada e uma mensagem de erro é exibida
     *
     * IMPORTÂNCIA: As validações evitam erros graves como:
     *  • Dar baixa com quantidade negativa (aumentaria o estoque!)
     *  • Dar baixa com mais do que disponível (criaria quantidade negativa)
     *  • Dar baixa com zero (operação inútil)
     *
     * @param qtd Quantidade a ser retirada
     */
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