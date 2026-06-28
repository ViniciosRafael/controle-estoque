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
 * Implementação baseada em Merge Sort, trocando a comparação numérica por
 * comparação alfabética (String) do nome do produto. Escolhido no lugar de
 * Insertion Sort por ter complexidade O(n log n) garantida (em vez de O(n²)),
 * o que escala muito melhor para os volumes de teste do projeto
 * (1.000 / 10.000 / 50.000 produtos).
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
        mergeSortPorNome(ordenados);
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
        mergeSortPorNome(ordenados);
        long fim = System.nanoTime();

        double tempoSegundos = (fim - inicio) / 1_000_000_000.0;
        return new ResultadoOrdenacaoPorNome<>(ordenados, tempoSegundos);
    }

    /**
     * Merge Sort por nome (ordem alfabética, ignorando maiúsculas/minúsculas).
     * Lógica: divide a lista recursivamente pela metade até sobrarem
     * sublistas de 0 ou 1 elemento (já "ordenadas" por definição), depois
     * intercala (merge) essas sublistas de volta, sempre escolhendo o menor
     * nome entre as duas frentes — complexidade O(n log n) garantida,
     * independente da ordem inicial dos dados.
     *
     * O ID de cada Produto não é tocado: ele é um atributo do próprio objeto,
     * que apenas muda de posição dentro da lista junto com o restante dos
     * seus dados (nome, categoria, preço, etc.).
     */
    private static <T extends Produto> void mergeSortPorNome(List<T> produtos) {
        int n = produtos.size();
        if (n < 2) return; // lista vazia ou de 1 elemento já está ordenada

        int meio = n / 2;
        List<T> esquerda = new ArrayList<>(produtos.subList(0, meio));
        List<T> direita   = new ArrayList<>(produtos.subList(meio, n));

        mergeSortPorNome(esquerda);
        mergeSortPorNome(direita);

        intercalar(produtos, esquerda, direita);
    }

    /**
     * Intercala (merge) duas sublistas já ordenadas — {@code esquerda} e
     * {@code direita} — escrevendo o resultado combinado e ordenado de
     * volta em {@code destino}.
     */
    private static <T extends Produto> void intercalar(List<T> destino, List<T> esquerda, List<T> direita) {
        int i = 0, j = 0, k = 0;
        int tamEsquerda = esquerda.size(), tamDireita = direita.size();

        while (i < tamEsquerda && j < tamDireita) {
            if (comparar(esquerda.get(i), direita.get(j)) <= 0) {
                destino.set(k++, esquerda.get(i++));
            } else {
                destino.set(k++, direita.get(j++));
            }
        }
        while (i < tamEsquerda) destino.set(k++, esquerda.get(i++));
        while (j < tamDireita)  destino.set(k++, direita.get(j++));
    }

    /** Compara dois produtos pelo nome, ignorando maiúsculas/minúsculas. */
    private static int comparar(Produto a, Produto b) {
        String nomeA = a.getNome() == null ? "" : a.getNome();
        String nomeB = b.getNome() == null ? "" : b.getNome();
        return nomeA.compareToIgnoreCase(nomeB);
    }
}