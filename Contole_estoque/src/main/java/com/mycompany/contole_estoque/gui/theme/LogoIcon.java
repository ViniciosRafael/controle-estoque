package com.mycompany.contole_estoque.gui.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Desenha o logo "Flow Control" via código.
 * Design focado em um "F" estilizado com setas de fluxo.
 */
public class LogoIcon implements Icon {
    private final int size;
    private final Color primaryColor = new Color(16, 163, 127);   // Verde escuro
    private final Color secondaryColor = new Color(180, 230, 215); // Verde claro

    public LogoIcon(int size) {
        this.size = size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);

        double scale = size / 100.0;
        g2.scale(scale, scale);

        // 1. Haste Vertical do "F" (Bem longa e visível)
        g2.setColor(primaryColor);
        g2.setStroke(new BasicStroke(9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(15, 15, 15, 85)); // Linha vertical principal

        // 2. Traço Superior do "F" (Seta de fluxo)
        g2.draw(new Line2D.Double(15, 20, 75, 20));
        Path2D arrow1 = new Path2D.Double();
        arrow1.moveTo(75, 12);
        arrow1.lineTo(88, 20);
        arrow1.lineTo(75, 28);
        arrow1.closePath();
        g2.fill(arrow1);

        // 3. Traço do Meio do "F" (Seta de fluxo menor)
        g2.draw(new Line2D.Double(15, 45, 60, 45));
        Path2D arrow2 = new Path2D.Double();
        arrow2.moveTo(60, 38);
        arrow2.lineTo(72, 45);
        arrow2.lineTo(60, 52);
        arrow2.closePath();
        g2.fill(arrow2);

        // 4. Detalhe de Rastreabilidade (Círculos/Nós na haste)
        g2.setColor(secondaryColor);
        g2.fill(new Ellipse2D.Double(10, 15, 10, 10));
        g2.fill(new Ellipse2D.Double(10, 40, 10, 10));
        g2.fill(new Ellipse2D.Double(10, 80, 10, 10));

        g2.dispose();
    }

    @Override public int getIconWidth() { return size; }
    @Override public int getIconHeight() { return size; }
}