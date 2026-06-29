package com.mycompany.contole_estoque.teste;

import com.mycompany.contole_estoque.LoteEstoque;
import com.mycompany.contole_estoque.Produto;
import com.mycompany.contole_estoque.ProdutoNaoPerecivel;
import com.mycompany.contole_estoque.ProdutoPerecivel;
import com.mycompany.contole_estoque.store.EstoqueStore;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gerador de massa de dados para testes de carga/performance do sistema.
 *
 * Gera N produtos perecíveis e N produtos não perecíveis (cada um com seu
 * lote de estoque associado), com valores aleatórios plausíveis, e insere
 * tudo direto no {@link EstoqueStore} (o singleton em memória usado por
 * toda a aplicação).
 *
 * Uso, dentro do código, chamando direto:
 *     GeradorDadosTeste.gerar(10_000);
 *
 * No projeto, essa chamada fica em Contole_estoque.main(), controlada pela
 * constante QUANTIDADE_DADOS_TESTE — troque o valor lá para testar com
 * volumes diferentes (ex: 1_000, 10_000, 50_000).
 */
public class GeradorDadosTeste {

    // ---- Dados-base usados para montar nomes e categorias aleatórios -----
    private static final String[] CATEGORIAS_PERECIVEL = {
        "Hortifruti", "Laticínios", "Carnes", "Padaria", "Congelados",
        "Bebidas", "Frios", "Embutidos"
    };

    private static final String[] CATEGORIAS_NAO_PERECIVEL = {
        "Limpeza", "Higiene", "Mercearia", "Eletrônicos", "Papelaria",
        "Utensílios", "Brinquedos", "Ferramentas"
    };

    private static final String[] NOMES_PERECIVEL = {
        "Leite", "Iogurte", "Queijo", "Presunto", "Frango", "Carne Moída",
        "Maçã", "Banana", "Alface", "Tomate", "Pão Francês", "Manteiga",
        "Requeijão", "Linguiça", "Peixe", "Mussarela", "Creme de Leite",
        "Suco Natural", "Salsicha", "Mortadela"
    };

    private static final String[] NOMES_NAO_PERECIVEL = {
        "Detergente", "Sabão em Pó", "Papel Higiênico", "Arroz", "Feijão",
        "Macarrão", "Óleo de Soja", "Açúcar", "Sal", "Café", "Pilha AA",
        "Caderno", "Caneta", "Lâmpada", "Vassoura", "Esponja", "Shampoo",
        "Sabonete", "Fio Dental", "Parafuso"
    };

    /**
     * Gera {@code quantidade} produtos perecíveis e {@code quantidade} produtos
     * não perecíveis (cada um com um lote de estoque), inserindo tudo no
     * EstoqueStore.
     *
     * @param quantidade número de produtos de CADA tipo a gerar
     *                   (o total de itens gerados será o dobro disso)
     */
    public static void gerar(int quantidade) {
        EstoqueStore store = EstoqueStore.get();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (int i = 0; i < quantidade; i++) {
            // ---------------- Produto perecível ----------------
            int idProdutoPerecivel = store.nextId();
            String nomePerecivel = NOMES_PERECIVEL[rnd.nextInt(NOMES_PERECIVEL.length)]
                    + " " + (i + 1);
            String categoriaPerecivel = CATEGORIAS_PERECIVEL[rnd.nextInt(CATEGORIAS_PERECIVEL.length)];
            int estoqueMinimoPerecivel = rnd.nextInt(5, 51);          // 5 a 50
            double precoPerecivel = arredondar(rnd.nextDouble(1.0, 100.0)); // R$ 1,00 a R$ 100,00

            ProdutoPerecivel produtoPerecivel = new ProdutoPerecivel(
                    idProdutoPerecivel, nomePerecivel, categoriaPerecivel,
                    estoqueMinimoPerecivel, precoPerecivel
            );
            store.getPerec().add(produtoPerecivel);

            LoteEstoque lotePerecivel = criarLoteAleatorio(store.nextId(), produtoPerecivel, rnd, true);
            store.getLotes().add(lotePerecivel);

            // ---------------- Produto não perecível ----------------
            int idProdutoNaoPerecivel = store.nextId();
            String nomeNaoPerecivel = NOMES_NAO_PERECIVEL[rnd.nextInt(NOMES_NAO_PERECIVEL.length)]
                    + " " + (i + 1);
            String categoriaNaoPerecivel = CATEGORIAS_NAO_PERECIVEL[rnd.nextInt(CATEGORIAS_NAO_PERECIVEL.length)];
            double precoNaoPerecivel = arredondar(rnd.nextDouble(1.0, 300.0)); // R$ 1,00 a R$ 300,00
            int estoqueMinimoNaoPerecivel = rnd.nextInt(5, 51);

            ProdutoNaoPerecivel produtoNaoPerecivel = new ProdutoNaoPerecivel(
                    idProdutoNaoPerecivel, nomeNaoPerecivel, categoriaNaoPerecivel,
                    precoNaoPerecivel, estoqueMinimoNaoPerecivel
            );
            store.getNaoPerec().add(produtoNaoPerecivel);

            LoteEstoque loteNaoPerecivel = criarLoteAleatorio(store.nextId(), produtoNaoPerecivel, rnd, false);
            store.getLotes().add(loteNaoPerecivel);
        }

        // Recalcula os alertas (vencimento / estoque mínimo) com a nova massa de dados
        store.gerarAlertas();
    }

    /**
     * Cria um lote de estoque aleatório para o produto informado.
     * Se {@code perecivel} for true, gera uma data de validade (algumas no
     * passado, para simular itens vencidos; a maioria no futuro).
     */
    private static LoteEstoque criarLoteAleatorio(int idLote, Produto produto,
                                                   ThreadLocalRandom rnd, boolean perecivel) {
        String numeroLote = "LOTE-" + idLote;
        int quantidade = rnd.nextInt(1, 501); // 1 a 500 unidades

        // Data de entrada: até 60 dias atrás
        LocalDate dataEntrada = LocalDate.now().minusDays(rnd.nextInt(0, 61));

        LocalDate dataValidade = null;
        if (perecivel) {
            // 10% de chance de já estar vencido (para testar alertas de vencimento)
            if (rnd.nextInt(100) < 10) {
                dataValidade = LocalDate.now().minusDays(rnd.nextInt(1, 31));
            } else {
                dataValidade = LocalDate.now().plusDays(rnd.nextInt(1, 181)); // até 180 dias no futuro
            }
        }

        return new LoteEstoque(idLote, numeroLote, produto, quantidade, dataEntrada, dataValidade);
    }

    /** Arredonda para 2 casas decimais (valores monetários). */
    private static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}