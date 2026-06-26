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
import javax.swing.*;

public class Contole_estoque {

    public static void main(String[] args) {
        // Configura o Look & Feel moderno antes de criar qualquer componente Swing
        FlatDarkLaf.setup();
        UIManager.put("Button.arc",          10);
        UIManager.put("Component.arc",       10);
        UIManager.put("TextComponent.arc",   10);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("TabbedPane.showTabSeparators", true);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
