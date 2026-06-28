/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.contole_estoque;

/**
 *
 * @author vinic
 */
import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.contole_estoque.gui.MainFrame;
import com.mycompany.contole_estoque.teste.GeradorDadosTeste;
import javax.swing.*;

public class Contole_estoque {

    /**
     * Quantidade de produtos de TESTE gerados automaticamente ao iniciar o
     * programa (de CADA tipo — perecível e não perecível; o total de itens
     * criados é o dobro desse valor). Troque este número para testar com
     * volumes diferentes, ex: 1_000, 10_000 ou 50_000.
     *
     * Defina como 0 para iniciar com o sistema vazio (sem dados de teste).
     */
    private static final int QUANTIDADE_DADOS_TESTE = 1_000;

    public static void main(String[] args) {
        // Configura o Look & Feel moderno antes de criar qualquer componente Swing
        FlatDarkLaf.setup();
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("TabbedPane.showTabSeparators", true);

        if (QUANTIDADE_DADOS_TESTE > 0) {
            GeradorDadosTeste.gerar(QUANTIDADE_DADOS_TESTE);
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}