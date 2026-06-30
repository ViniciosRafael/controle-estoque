package com.mycompany.contole_estoque;
import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.contole_estoque.gui.MainFrame;
import com.mycompany.contole_estoque.teste.GeradorDadosTeste;
import javax.swing.*;

/**
 * Classe principal do sistema de controle de estoque.
 * Ponto inicial da aplicação que configura a interface e inicia a janela principal.
 */
public class Contole_estoque {

    // Define a quantidade de registros fictícios a serem gerados (0 desativa a geração de testes)
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
        FlatDarkLaf.setup();  // Ativa o tema escuro
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