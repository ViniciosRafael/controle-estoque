package com.mycompany.contole_estoque.util;

import com.mycompany.contole_estoque.Produto;
import java.util.ArrayList;
import java.util.List;

/**
 * Organiza Produtos em ordem alfabética pelo nome, preservando o ID com o
 * qual cada produto foi originalmente cadastrado.
 *
 * Como o ID é um atributo do próprio objeto Produto (não depende da posição
 * dele numa lista ou tabela), ordenar a lista por nome não altera o ID de
 * nenhum produto — apenas a posição em que cada um aparece no resultado.
 *
 * Implementação baseada em Insertion Sort (adaptada do benchmark de
 * algoritmos de ordenação do projeto de catálogo de filmes), trocando a
 * comparação numérica por comparação alfabética (String) do nome do produto.
 */
public class OrdenadorProdutosPorNome {

    /**
     * Resultado de uma ordenação com medição de tempo: contém a lista já
     * ordenada e o tempo que a ordenação levou para ser executada.
     */
    public static class ResultadoOrdenacaoPorNome<T extends Produto> {
        private final List<T> produtosOrdenados;
        private final double tempoSegundos;

        public ResultadoOrdenacaoPorNome(List<T> produtosOrdenados, double tempoSegundos) {
            this.produtosOrdenados = produtosOrdenados;
            this.tempoSegundos = tempoSegundos;
        }

        public List<T> getProdutosOrdenados() { return produtosOrdenados; }

        /** Tempo de execução da ordenação, em segundos. */
        public double getTempoSegundos() { return tempoSegundos; }

        /** Tempo de execução da ordenação, em milissegundos (mais fácil de exibir na tela). */
        public double getTempoMilissegundos() { return tempoSegundos * 1000.0; }
    }

    /**
     * Retorna uma NOVA lista com os produtos ordenados alfabeticamente pelo
     * nome (ordem A-Z, sem diferenciar maiúsculas/minúsculas). A lista
     * recebida como parâmetro não é modificada; o ID de cada Produto
     * permanece exatamente o mesmo que tinha antes da ordenação.
     */
    public static <T extends Produto> List<T> ordenarPorNome(List<T> produtos) {
        List<T> ordenados = new ArrayList<>(produtos); // cópia: preserva a lista original
        insertionSortPorNome(ordenados);
        return ordenados;
    }

    /**
     * Mesmo comportamento de {@link #ordenarPorNome(List)}, mas também mede
     * quanto tempo a ordenação levou para ser executada.
     *
     * Útil para acompanhar o desempenho do algoritmo conforme a quantidade
     * de produtos cadastrados cresce (igual à medição de tempo feita no
     * benchmark de algoritmos de ordenação do projeto de catálogo de filmes).
     */
    public static <T extends Produto> ResultadoOrdenacaoPorNome<T> ordenarPorNomeComTempo(List<T> produtos) {
        List<T> ordenados = new ArrayList<>(produtos); // cópia: preserva a lista original

        long inicio = System.nanoTime();
        insertionSortPorNome(ordenados);
        long fim = System.nanoTime();

        double tempoSegundos = (fim - inicio) / 1_000_000_000.0;
        return new ResultadoOrdenacaoPorNome<>(ordenados, tempoSegundos);
    }

    /**
     * Insertion Sort por nome (ordem alfabética, ignorando maiúsculas/minúsculas).
     * Lógica: desloca produtos cujo nome vem depois do nome do produto atual
     * para a direita, até achar a posição correta para inserí-lo — mesma
     * lógica do Insertion Sort numérico, mas comparando nomes em vez de IDs.
     *
     * O ID de cada Produto não é tocado: ele é um atributo do próprio objeto,
     * que apenas muda de posição dentro da lista junto com o restante dos
     * seus dados (nome, categoria, preço, etc.).
     */
    private static <T extends Produto> void insertionSortPorNome(List<T> produtos) {
        int n = produtos.size();
        for (int i = 1; i < n; i++) {
            T chave = produtos.get(i);
            int j = i - 1;
            while (j >= 0 && comparar(produtos.get(j), chave) > 0) {
                produtos.set(j + 1, produtos.get(j));
                j--;
            }
            produtos.set(j + 1, chave);
        }
    }

    /** Compara dois produtos pelo nome, ignorando maiúsculas/minúsculas. */
    private static int comparar(Produto a, Produto b) {
        String nomeA = a.getNome() == null ? "" : a.getNome();
        String nomeB = b.getNome() == null ? "" : b.getNome();
        return nomeA.compareToIgnoreCase(nomeB);
    }
}