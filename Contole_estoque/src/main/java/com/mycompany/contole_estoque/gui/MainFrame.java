package com.mycompany.contole_estoque.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.event.*;

public class MainFrame extends JFrame {

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
    private ConfiguracoesPanel configuracoesPanel;

    public MainFrame() {
        setTitle("Flow Control");
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
        configuracoesPanel = new ConfiguracoesPanel();

        contentPanel.add(dashboardPanel,  "dashboard");
        contentPanel.add(produtosPanel,   "produtos");
        contentPanel.add(lotesPanel,      "estoque");
        contentPanel.add(descartesPanel,  "historico");
        contentPanel.add(configuracoesPanel, "configuracoes");

        cardLayout.show(contentPanel, "dashboard");
        return contentPanel;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, SIDEBAR_BORD));

        // Seção do Logo
        sidebar.add(buildLogoSection());
        
        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(SIDEBAR_BORD);
        sep.setMaximumSize(new Dimension(210, 1));
        sidebar.add(sep);
        
        sidebar.add(Box.createVerticalStrut(15));

        // Label MENU
        JLabel navLabel = new JLabel("  MENU");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(new Color(160, 163, 180));
        navLabel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(navLabel);
        
        sidebar.add(Box.createVerticalStrut(8));

        // Itens de Navegação
        String[][] items = {
            {"Dashboard",      "dashboard"},
            {"Produtos",       "produtos"},
            {"Estoque",        "estoque"},
            {"Histórico",      "historico"},
            {"Configurações",  "configuracoes"},
        };

        for (String[] item : items) {
            JButton btn = navButton(item[0], item[1]);
            sidebar.add(btn);
            if (item[1].equals("dashboard")) activateButton(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel buildLogoSection() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        p.setBackground(SIDEBAR_BG);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(210, 90));

        // Logo desenhado via código
        JLabel lblLogo = new JLabel(new com.mycompany.contole_estoque.gui.theme.LogoIcon(40));
        p.add(lblLogo);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel title = new JLabel("FlowControl");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(20, 22, 35));
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("GESTÃO DE ESTOQUE");
        sub.setFont(new Font("Segoe UI", Font.BOLD, 9));
        sub.setForeground(new Color(120, 123, 140));
        sub.setAlignmentX(LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(sub);
        p.add(textPanel);

        return p;
    }

    private JButton navButton(String label, String card) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_NAV);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(210, 40));
        btn.setPreferredSize(new Dimension(210, 40));
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
            case "dashboard"     -> dashboardPanel.refresh();
            case "produtos"      -> produtosPanel.refresh();
            case "estoque"       -> lotesPanel.refresh();
            case "historico"     -> descartesPanel.refresh();
            case "configuracoes" -> {
                try { configuracoesPanel.refresh(); } catch (Exception e) {}
            }
        }
    }
}