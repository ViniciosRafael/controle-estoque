package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.gui.dialogs.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Janela principal — sidebar clara e área de conteúdo branca.
 */
public class MainFrame extends JFrame {

    // ── paleta sidebar clara
    private static final Color SIDEBAR_BG   = new Color(248, 249, 252);
    private static final Color SIDEBAR_BORD = new Color(220, 222, 230);
    private static final Color ACTIVE_BG    = new Color(0, 120, 210);
    private static final Color ACTIVE_FG    = Color.WHITE;
    private static final Color HOVER_BG     = new Color(232, 236, 245);
    private static final Color HOVER_FG     = new Color(20, 22, 35);
    private static final Color TEXT_NAV     = new Color(70, 75, 95);
    private static final Color CONTENT_BG   = Color.WHITE;

    private CardLayout  cardLayout;
    private JPanel      contentPanel;
    private JButton     activeButton;

    private DashboardPanel  dashboardPanel;
    private ProdutosPanel   produtosPanel;
    private LotesPanel      lotesPanel;
    private DescartesPanel  descartesPanel;

    public MainFrame() {
        setTitle("Food Control");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);

        dashboardPanel  = new DashboardPanel();
        produtosPanel   = new ProdutosPanel();
        lotesPanel      = new LotesPanel();
        descartesPanel  = new DescartesPanel();

        contentPanel.add(dashboardPanel,  "dashboard");
        contentPanel.add(produtosPanel,   "produtos");
        contentPanel.add(lotesPanel,      "estoque");
        contentPanel.add(descartesPanel,  "descartes");

        cardLayout.show(contentPanel, "dashboard");
        return contentPanel;
    }

    // ================================================================ sidebar
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(170, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, SIDEBAR_BORD));

        sidebar.add(buildLogo());
        sidebar.add(buildSep());
        sidebar.add(Box.createVerticalStrut(10));

        JLabel nav = new JLabel("  MENU");
        nav.setFont(new Font("Segoe UI", Font.BOLD, 10));
        nav.setForeground(new Color(160, 163, 180));
        nav.setMaximumSize(new Dimension(170, 22));
        sidebar.add(nav);
        sidebar.add(Box.createVerticalStrut(4));

        String[][] items = {
            {"Dashboard",  "dashboard"},
            {"Produtos",   "produtos"},
            {"Estoque",    "estoque"},
            {"Descartes",  "descartes"},
        };

        boolean first = true;
        for (String[] item : items) {
            JButton btn = navButton(item[0], item[1]);
            sidebar.add(btn);
            if (first) { activateButton(btn); first = false; }
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("  v1.0");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ver.setForeground(new Color(180, 183, 195));
        ver.setBorder(new EmptyBorder(8, 10, 12, 10));
        sidebar.add(ver);

        return sidebar;
    }

    private Component buildLogo() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(SIDEBAR_BG);
        p.setBorder(new EmptyBorder(18, 14, 14, 14));
        p.setMaximumSize(new Dimension(170, 72));

        JLabel title = new JLabel("Food Control");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Controle de Estoque");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sub.setForeground(new Color(150, 153, 170));
        sub.setAlignmentX(LEFT_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(2));
        p.add(sub);
        return p;
    }

    private Component buildSep() {
        JSeparator s = new JSeparator();
        s.setForeground(SIDEBAR_BORD);
        s.setMaximumSize(new Dimension(170, 1));
        return s;
    }

    private JButton navButton(String label, String card) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_NAV);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(170, 38));
        btn.setPreferredSize(new Dimension(170, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(HOVER_BG); btn.setForeground(HOVER_FG); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(SIDEBAR_BG); btn.setForeground(TEXT_NAV); }
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
        btn.setForeground(ACTIVE_FG);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void deactivateButton(JButton btn) {
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(TEXT_NAV);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void refreshPanel(String card) {
        switch (card) {
            case "dashboard" -> dashboardPanel.refresh();
            case "produtos"  -> produtosPanel.refresh();
            case "estoque"   -> lotesPanel.refresh();
            case "descartes" -> descartesPanel.refresh();
        }
    }
}
