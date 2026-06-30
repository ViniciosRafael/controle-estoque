package com.mycompany.contole_estoque;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                    CONTROLE DE ESTOQUE - CLASSE PRINCIPAL                  ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 *
 * Esta é a classe principal do sistema de Controle de Estoque.
 * Responsabilidades:
 *  • Inicializar a aplicação Swing com o tema moderno FlatDarkLaf
 *  • Configurar as propriedades visuais da interface (cantos arredondados, etc)
 *  • Carregar dados de teste (opcional) para testes de performance
 *  • Exibir a janela principal (MainFrame) na thread de eventos Swing
 *
 * O sistema gerencia:
 *  - Produtos (perecíveis e não perecíveis)
 *  - Lotes de estoque com datas de validade
 *  - Alertas de vencimento e estoque mínimo
 *  - Histórico de movimentações (entrada, saída, descarte)
 *  - Cálculo de prejuízos e exportação para Excel
 */
import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.contole_estoque.gui.MainFrame;
import com.mycompany.contole_estoque.teste.GeradorDadosTeste;
import javax.swing.*;

public class Contole_estoque {

    /**
     * Quantidade de produtos de TESTE gerados automaticamente ao iniciar o programa.
     * Valor duplicado (de CADA tipo — perecível E não perecível; o total real é o dobro).
     *
     * CONFIGURAÇÃO:
     *  0         → Inicia com sistema vazio (sem dados de teste)
     *  100       → Teste rápido com dados mínimos
     *  500       → Teste padrão com volume moderado (padrão do sistema)
     *  1_000     → Teste com volume maior
     *  10_000    → Teste de carga/performance
     *  50_000    → Teste de estresse do sistema
     */
    private static final int QUANTIDADE_DADOS_TESTE = 500;

    /**
     * Método principal - Ponto de entrada do sistema.
     * Sequência de inicialização:
     *   1. Configura o tema visual FlatDarkLaf (look & feel moderno escuro)
     *   2. Personifica componentes Swing (botões, campos, abas)
     *   3. Gera dados de teste (se QUANTIDADE_DADOS_TESTE > 0)
     *   4. Cria e exibe a janela principal (MainFrame)
     */
    public static void main(String[] args) {
        // ═══════════════════════════════════════════════════════════════════
        // PASSO 1: Configurar tema visual moderno e componentes Swing
        // ═══════════════════════════════════════════════════════════════════
        FlatDarkLaf.setup();  // Ativa o tema escuro moderno com aparência profissional
        UIManager.put("Button.arc", 10);              // Cantos arredondados em botões
        UIManager.put("Component.arc", 10);           // Cantos arredondados em componentes
        UIManager.put("TextComponent.arc", 10);       // Cantos arredondados em campos de texto
        UIManager.put("ScrollBar.showButtons", false);// Oculta botões da barra de rolagem
        UIManager.put("TabbedPane.showTabSeparators", true); // Exibe separadores entre abas

        // ═══════════════════════════════════════════════════════════════════
        // PASSO 2: Gerar dados de teste (opcional, para testes/demonstração)
        // ═══════════════════════════════════════════════════════════════════
        if (QUANTIDADE_DADOS_TESTE > 0) {
            GeradorDadosTeste.gerar(QUANTIDADE_DADOS_TESTE);
        }

        // ═══════════════════════════════════════════════════════════════════
        // PASSO 3: Criar e exibir a janela principal na thread de eventos Swing
        // ═══════════════════════════════════════════════════════════════════
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}