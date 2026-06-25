package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.gui.dialogs.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Janela principal da aplicação.
 * Contém uma sidebar de navegação fixa e uma área central com CardLayout.
 */
public class MainFrame extends JFrame {

    private static final Color SIDEBAR_BG   = new Color(18, 18, 30);
    private static final Color ACTIVE_BG    = new Color(30, 90, 150);
    private static final Color HOVER_BG     = new Color(35, 35, 58);
    private static final Color TEXT_DIM     = new Color(140, 140, 165);

    private CardLayout   cardLayout;
    private JPanel       contentPanel;
    private JButton      activeButton;

    private DashboardPanel  dashboardPanel;
    private ProdutosPanel   produtosPanel;
    private LotesPanel      lotesPanel;
    private DescartesPanel  descartesPanel;
    private AlertasPanel    alertasPanel;

    public MainFrame() {
        setTitle("Controle de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);
        buildUI();
    }

    // ------------------------------------------------------------------ UI
    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        dashboardPanel  = new DashboardPanel();
        produtosPanel   = new ProdutosPanel();
        lotesPanel      = new LotesPanel();
        descartesPanel  = new DescartesPanel();
        alertasPanel    = new AlertasPanel();

        contentPanel.add(dashboardPanel,  "dashboard");
        contentPanel.add(produtosPanel,   "produtos");
        contentPanel.add(lotesPanel,      "lotes");
        contentPanel.add(descartesPanel,  "descartes");
        contentPanel.add(alertasPanel,    "alertas");

        cardLayout.show(contentPanel, "dashboard");
        return contentPanel;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));

        // ── logo
        sidebar.add(buildLogo());
        sidebar.add(buildSeparator());
        sidebar.add(Box.createVerticalStrut(8));

        // ── nav label
        JLabel nav = new JLabel("  NAVEGAÇÃO");
        nav.setFont(new Font("Segoe UI", Font.BOLD, 10));
        nav.setForeground(TEXT_DIM);
        nav.setMaximumSize(new Dimension(230, 24));
        sidebar.add(nav);
        sidebar.add(Box.createVerticalStrut(6));

        // ── nav buttons
        String[][] items = {
            {"🏠", "Dashboard",  "dashboard"},
            {"📦", "Produtos",   "produtos"},
            {"🗂️", "Lotes",      "lotes"},
            {"🗑️", "Descartes",  "descartes"},
            {"🔔", "Alertas",    "alertas"},
        };

        boolean first = true;
        for (String[] item : items) {
            JButton btn = navButton(item[0], item[1], item[2]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(3));
            if (first) { activateButton(btn); first = false; }
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("  v1.0  •  2025");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ver.setForeground(TEXT_DIM);
        ver.setBorder(new EmptyBorder(10, 10, 12, 10));
        sidebar.add(ver);

        return sidebar;
    }

    private Component buildLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        p.setBackground(SIDEBAR_BG);
        p.setMaximumSize(new Dimension(230, 72));

        JLabel icon = new JLabel("📦");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(SIDEBAR_BG);

        JLabel title = new JLabel("Estoque Pro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Controle de Estoque");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sub.setForeground(TEXT_DIM);

        text.add(title);
        text.add(sub);
        p.add(icon);
        p.add(text);
        return p;
    }

    private Component buildSeparator() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(45, 45, 65));
        s.setMaximumSize(new Dimension(230, 1));
        return s;
    }

    private JButton navButton(String icon, String label, String card) {
        JButton btn = new JButton(icon + "   " + label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_DIM);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 44));
        btn.setPreferredSize(new Dimension(220, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(HOVER_BG); btn.setForeground(Color.WHITE); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(SIDEBAR_BG); btn.setForeground(TEXT_DIM); }
            }
        });

        btn.addActionListener(e -> {
            if (activeButton != null) deactivateButton(activeButton);
            activateButton(btn);
            cardLayout.show(contentPanel, card);
            refreshPanel(card);
        });

        return btn;
    }

    private void activateButton(JButton btn) {
        activeButton = btn;
        btn.setBackground(ACTIVE_BG);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void deactivateButton(JButton btn) {
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(TEXT_DIM);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void refreshPanel(String card) {
        switch (card) {
            case "dashboard"  -> dashboardPanel.refresh();
            case "produtos"   -> produtosPanel.refresh();
            case "lotes"      -> lotesPanel.refresh();
            case "descartes"  -> descartesPanel.refresh();
            case "alertas"    -> alertasPanel.refresh();
        }
    }
}
