package com.mycompany.contole_estoque.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.event.*;

/**
 * Esta é a janela principal do programa.
 * Ela contém a barra lateral (menu) e a área onde as telas mudam.
 */
public class MainFrame extends JFrame {

    // Definição das cores da barra lateral e do conteúdo
    private static final Color SIDEBAR_BG   = new Color(248, 249, 252); // Fundo do menu
    private static final Color SIDEBAR_BORD = new Color(220, 222, 230); // Borda do menu
    private static final Color ACTIVE_BG    = new Color(0, 120, 210);   // Cor do botão selecionado
    private static final Color ACTIVE_FG    = Color.WHITE;              // Cor do texto do botão selecionado
    private static final Color HOVER_BG     = new Color(232, 236, 245); // Cor quando passa o mouse
    private static final Color HOVER_FG     = new Color(20, 22, 35);
    private static final Color TEXT_NAV     = new Color(70, 75, 95);    // Cor do texto do menu
    private static final Color CONTENT_BG   = Color.WHITE;              // Fundo da área principal

    private CardLayout  cardLayout;   // Gerenciador que permite trocar de tela como se fossem cartões
    private JPanel      contentPanel; // Painel que segura todas as telas
    private JButton     activeButton; // Guarda qual botão do menu está clicado no momento

    // As diferentes telas do sistema
    private DashboardPanel  dashboardPanel;
    private ProdutosPanel   produtosPanel;
    private LotesPanel      lotesPanel;
    private DescartesPanel  descartesPanel;
    private ConfiguracoesPanel configuracoesPanel;

    public MainFrame() {
        setTitle("Flow Control"); // Nome que aparece no topo da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao clicar no X
        setSize(1280, 780); // Tamanho inicial da janela
        setMinimumSize(new Dimension(900, 580)); // Tamanho mínimo que a janela pode ter
        setLocationRelativeTo(null); // Centraliza a janela na tela do computador
        buildUI(); // Monta a interface
    }

    // Monta a estrutura da janela (Menu na esquerda, Conteúdo no centro)
    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    // Cria o painel onde as telas (Dashboard, Produtos, etc.) serão trocadas
    private JPanel buildContent() {
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);

        // Inicializa cada uma das telas
        dashboardPanel  = new DashboardPanel();
        produtosPanel   = new ProdutosPanel();
        lotesPanel      = new LotesPanel();
        descartesPanel  = new DescartesPanel();
        configuracoesPanel = new ConfiguracoesPanel();

        // Adiciona as telas ao painel de conteúdo com nomes de identificação
        contentPanel.add(dashboardPanel,  "dashboard");
        contentPanel.add(produtosPanel,   "produtos");
        contentPanel.add(lotesPanel,      "estoque");
        contentPanel.add(descartesPanel,  "historico");
        contentPanel.add(configuracoesPanel, "configuracoes");

        cardLayout.show(contentPanel, "dashboard"); // Mostra o Dashboard primeiro
        return contentPanel;
    }

    // Cria a barra lateral com o logo e os botões do menu
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(185, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, SIDEBAR_BORD));

        sidebar.add(buildLogo()); // Adiciona o logo no topo
        sidebar.add(buildSep());
        sidebar.add(Box.createVerticalStrut(10));

        JLabel nav = new JLabel("  MENU");
        nav.setFont(new Font("Segoe UI", Font.BOLD, 10));
        nav.setForeground(new Color(160, 163, 180));
        nav.setMaximumSize(new Dimension(185, 22));
        sidebar.add(nav);
        sidebar.add(Box.createVerticalStrut(4));

        // Lista de itens do menu (Texto do botão e Nome da tela)
        String[][] items = {
            {"Dashboard",      "dashboard"},
            {"Produtos",       "produtos"},
            {"Estoque",        "estoque"},
            {"Histórico",      "historico"},
            {"Configurações",  "configuracoes"},
        };

        boolean first = true;
        for (String[] item : items) {
            JButton btn = navButton(item[0], item[1]);
            sidebar.add(btn);
            if (first) { activateButton(btn); first = false; }
        }

        sidebar.add(Box.createVerticalGlue()); // Empurra o que vem abaixo para o final da tela

        JLabel ver = new JLabel("  v1.1");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ver.setForeground(new Color(180, 183, 195));
        ver.setBorder(new EmptyBorder(8, 10, 12, 10));
        sidebar.add(ver);

        return sidebar;
    }

    // Cria o texto do Logo que aparece no menu
    private Component buildLogo() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(SIDEBAR_BG);
        p.setBorder(new EmptyBorder(18, 14, 14, 14));
        p.setMaximumSize(new Dimension(185, 72));

        JLabel title = new JLabel("Flow  Control");
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
        s.setMaximumSize(new Dimension(185, 1));
        return s;
    }

    // Cria um botão do menu e define o que acontece ao clicar nele
    private JButton navButton(String label, String card) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_NAV);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(185, 38));
        btn.setPreferredSize(new Dimension(185, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));

        // Muda a cor quando o mouse passa por cima
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(HOVER_BG); btn.setForeground(HOVER_FG); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(SIDEBAR_BG); btn.setForeground(TEXT_NAV); }
            }
        });

        // Quando clica, troca a tela principal e atualiza os dados da tela
        btn.addActionListener(e -> {
            if (activeButton != null) deactivateButton(activeButton);
            activateButton(btn);
            cardLayout.show(contentPanel, card);
            refreshPanel(card);
        });

        return btn;
    }

    // Destaca o botão selecionado
    private void activateButton(JButton btn) {
        activeButton = btn;
        btn.setBackground(ACTIVE_BG);
        btn.setForeground(ACTIVE_FG);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    // Remove o destaque do botão anterior
    private void deactivateButton(JButton btn) {
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(TEXT_NAV);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    // Faz a tela atualizar os dados (reler as listas) sempre que o usuário mudar de aba
    private void refreshPanel(String card) {
        switch (card) {
            case "dashboard"     -> dashboardPanel.refresh();
            case "produtos"      -> produtosPanel.refresh();
            case "estoque"       -> lotesPanel.refresh();
            case "historico"     -> descartesPanel.refresh();
            case "configuracoes" -> configuracoesPanel.refresh();
        }
    }
}
