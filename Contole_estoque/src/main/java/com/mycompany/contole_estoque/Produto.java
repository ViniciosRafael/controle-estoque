package com.mycompany.contole_estoque;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                        CLASSE BASE: PRODUTO                               ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Classe abstrata que define a estrutura comum a todos os produtos do estoque.
 * Todos os produtos, sejam perecíveis ou não perecíveis, herdam dessa classe.
 *
 * Atributos:
 *  • id          : Identificador único do produto no sistema
 *  • nome        : Nome descritivo do produto (ex: "Leite Integral", "Detergente")
 *  • categoria   : Classificação do produto (ex: "Laticínios", "Limpeza")
 *
 * Responsabilidades:
 *  • Armazenar dados básicos do produto
 *  • Fornecer método abstrato para calcular prejuízo (implementado pelas subclasses)
 *  • Permitir que subclasses definam estoque mínimo
 *  • Registrar o produto no sistema (log/auditoria)
 *
 * Subclasses:
 *  • ProdutoPerecivel   : Produtos com data de validade (ex: alimentos)
 *  • ProdutoNaoPerecivel: Produtos sem data de validade (ex: higiene, limpeza)
 */
public abstract class Produto {
    private int    id;        // ID único do produto
    private String nome;      // Nome do produto
    private String categoria; // Categoria/classificação

    public Produto() {}

    /**
     * Construtor completo do Produto.
     * @param id        : Identificador único
     * @param nome      : Nome descritivo
     * @param categoria : Classificação do produto
     */
    public Produto(int id, String nome, String categoria) {
        this.id        = id;
        this.nome      = nome;
        this.categoria = categoria;
    }

    // ═══════════════════════════════════════════════════════════════════
    // GETTERS - Recuperam dados do produto
    // ═══════════════════════════════════════════════════════════════════
    public String getNome()      { return this.nome;      }
    public int    getId()        { return this.id;        }
    public String getCategoria() { return this.categoria; }

    // ═══════════════════════════════════════════════════════════════════
    // SETTERS - Modificam dados do produto
    // ═══════════════════════════════════════════════════════════════════
    public void setNome(String nome)         { this.nome = nome;         }
    public void setId(int id)                { this.id   = id;           }
    public void setCategoria(String cat)     { this.categoria = cat;     }

    /**
     * Retorna o estoque mínimo configurado para o produto.
     * Implementação padrão retorna 0 (sem limite mínimo).
     * Subclasses que controlam estoque mínimo devem sobrescrever este método.
     *
     * @return Quantidade mínima desejada em estoque (0 se não definido)
     */
    public int getEstoqueMinimo() { return 0; }

    /**
     * Método abstrato para calcular o prejuízo causado por este produto.
     * O prejuízo é calculado com base no preço unitário.
     * Cada subclasse define sua própria lógica de cálculo.
     *
     * @return Valor do prejuízo em reais (R$)
     */
    public abstract double calcularPrejuizo();

    /**
     * Registra este produto no sistema.
     * Exibe mensagem de confirmação no console (para auditoria/log).
     * Pode ser estendido para registrar em banco de dados ou arquivo.
     */
    public void cadastrar() {
        System.out.println("Produto '" + this.nome + "' cadastrado com sucesso no sistema.");
    }
}