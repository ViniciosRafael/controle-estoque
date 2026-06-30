package com.mycompany.contole_estoque.gui; // Define o pacote desta classe

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame { // Declara a classe do formulário principal herdando JFrame

    private static final Color SIDEBAR_BG   = new Color(248, 249, 252); // Cor de fundo da barra lateral
    private static final Color SIDEBAR_BORD = new Color(220, 222, 230); // Cor da borda da barra lateral
    private static final Color ACTIVE_BG    = new Color(0, 120, 210); // Cor de fundo do botão ativo
    private static final Color ACTIVE_FG    = Color.WHITE; // Cor do texto do botão ativo
    private static final Color HOVER_BG     = new Color(232, 236, 245); // Cor de fundo no hover
    private static final Color HOVER_FG     = new Color(20, 22, 35); // Cor do texto no hover
    private static final Color TEXT_NAV     = new Color(70, 75, 95); // Cor normal do texto de navegação
    private static final Color CONTENT_BG   = Color.WHITE; // Cor de fundo do painel de conteúdo

    private CardLayout  cardLayout; // Declara o gerenciador de layout CardLayout
    private JPanel      contentPanel; // Declara o painel principal de conteúdo
    private JButton     activeButton; // Declara a variável que guarda o botão ativo atual

    private DashboardPanel  dashboardPanel; // Declara o painel de dashboard
    private ProdutosPanel   produtosPanel; // Declara o painel de produtos
    private LotesPanel      lotesPanel; // Declara o painel de lotes
    private DescartesPanel  descartesPanel; // Declara o painel de descartes
    private ConfiguracoesPanel configuracoesPanel; // Declara o painel de configurações

    public MainFrame() { // Construtor da classe MainFrame
        setTitle("Flow Control"); // Define o título da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a ação de fechar o programa ao fechar a janela
        setSize(1280, 780); // Configura o tamanho inicial da janela
        setMinimumSize(new Dimension(900, 580)); // Configura as dimensões mínimas
        setLocationRelativeTo(null); // Centraliza a janela na tela
        buildUI(); // Chama o método que constrói a interface de usuário
    } // Fim do construtor

    private void buildUI() { // Método para montar a UI
        setLayout(new BorderLayout()); // Define BorderLayout para o frame principal
        add(buildSidebar(), BorderLayout.WEST); // Adiciona a barra lateral na região oeste
        add(buildContent(), BorderLayout.CENTER); // Adiciona o conteúdo na região central
    } // Fim de buildUI

    private JPanel buildContent() { // Método que constrói o painel de conteúdo central
        cardLayout   = new CardLayout(); // Inicializa o CardLayout
        contentPanel = new JPanel(cardLayout); // Cria o painel associando-o ao CardLayout
        contentPanel.setBackground(CONTENT_BG); // Define a cor de fundo do conteúdo

        dashboardPanel  = new DashboardPanel(); // Inicializa o DashboardPanel
        produtosPanel   = new ProdutosPanel(); // Inicializa o ProdutosPanel
        lotesPanel      = new LotesPanel(); // Inicializa o LotesPanel
        descartesPanel  = new DescartesPanel(); // Inicializa o DescartesPanel
        configuracoesPanel = new ConfiguracoesPanel(); // Inicializa o ConfiguracoesPanel

        contentPanel.add(dashboardPanel,  "dashboard"); // Adiciona o dashboardPanel no CardLayout
        contentPanel.add(produtosPanel,   "produtos"); // Adiciona o produtosPanel no CardLayout
        contentPanel.add(lotesPanel,      "estoque"); // Adiciona o lotesPanel no CardLayout
        contentPanel.add(descartesPanel,  "historico"); // Adiciona o descartesPanel no CardLayout
        contentPanel.add(configuracoesPanel, "configuracoes"); // Adiciona o configuracoesPanel no CardLayout

        cardLayout.show(contentPanel, "dashboard"); // Exibe inicialmente o painel "dashboard"
        return contentPanel; // Retorna o painel de conteúdo construído
    } // Fim de buildContent

    private JPanel buildSidebar() { // Método que constrói a barra lateral
        JPanel sidebar = new JPanel(); // Cria o painel da barra lateral
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Define BoxLayout vertical para a sidebar
        sidebar.setBackground(SIDEBAR_BG); // Configura a cor de fundo da sidebar
        sidebar.setPreferredSize(new Dimension(210, 0)); // Fixa a largura preferida em 210 pixels
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, SIDEBAR_BORD)); // Adiciona uma borda na lateral direita

        // Seção do Logo
        sidebar.add(buildLogoSection()); // Adiciona a seção do logotipo na sidebar
        
        // Separador
        JSeparator sep = new JSeparator(); // Cria um separador visual
        sep.setForeground(SIDEBAR_BORD); // Define a cor do separador
        sep.setMaximumSize(new Dimension(210, 1)); // Limita a altura do separador a 1 pixel
        sidebar.add(sep); // Adiciona o separador na barra lateral
        
        sidebar.add(Box.createVerticalStrut(15)); // Insere um espaçamento vertical de 15 pixels

        // Label MENU
        JLabel navLabel = new JLabel("  MENU"); // Cria o label indicador do menu
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10)); // Define a fonte em negrito
        navLabel.setForeground(new Color(160, 163, 180)); // Aplica a cor do texto do label
        navLabel.setAlignmentX(LEFT_ALIGNMENT); // Alinha o componente à esquerda
        sidebar.add(navLabel); // Adiciona o label na barra lateral
        
        sidebar.add(Box.createVerticalStrut(8)); // Insere um espaçamento vertical de 8 pixels

        // Itens de Navegação
        String[][] items = { // Declara matriz com nomes e chaves de identificação
            {"Dashboard",      "dashboard"}, // Define título e identificador do dashboard
            {"Produtos",       "produtos"}, // Define título e identificador dos produtos
            {"Estoque",        "estoque"}, // Define título e identificador do estoque
            {"Histórico",      "historico"}, // Define título e identificador do histórico
            {"Configurações",  "configuracoes"}, // Define título e identificador das configurações
        }; // Fim da matriz de itens

        for (String[] item : items) { // Loop sobre todos os itens declarados
            JButton btn = navButton(item[0], item[1]); // Gera o botão usando o título e chave
            sidebar.add(btn); // Insere o botão na sidebar
            if (item[1].equals("dashboard")) activateButton(btn); // Define como ativo se for dashboard
        } // Encerra o loop for

        sidebar.add(Box.createVerticalGlue()); // Adiciona preenchimento vertical expansível no final

        return sidebar; // Retorna a barra lateral completamente construída
    } // Fim de buildSidebar

    private JPanel buildLogoSection() { // Método para montar a área da logotipo
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20)); // Instancia painel para logotipo com margens
        p.setBackground(SIDEBAR_BG); // Atribui a mesma cor de fundo da sidebar
        p.setAlignmentX(LEFT_ALIGNMENT); // Garante alinhamento à esquerda
        p.setMaximumSize(new Dimension(210, 90)); // Limita o espaço vertical ocupado

        // Logo desenhado via código
        JLabel lblLogo = new JLabel(new com.mycompany.contole_estoque.gui.theme.LogoIcon(40)); // Cria um label portando um ícone vetorial
        p.add(lblLogo); // Acrescenta o label com logo ao painel

        JPanel textPanel = new JPanel(); // Cria o painel para agrupar os textos ao lado da logo
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Organiza textos na vertical
        textPanel.setOpaque(false); // Torna o painel transparente

        JLabel title = new JLabel("FlowControl"); // Rótulo do título principal
        title.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modifica tamanho e peso da fonte do título
        title.setForeground(new Color(20, 22, 35)); // Define a cor predominante do título
        title.setAlignmentX(LEFT_ALIGNMENT); // Alinha o texto do título à esquerda

        JLabel sub = new JLabel("GESTÃO DE ESTOQUE"); // Rótulo de subtítulo explicativo
        sub.setFont(new Font("Segoe UI", Font.BOLD, 9)); // Usa fonte reduzida para o subtítulo
        sub.setForeground(new Color(120, 123, 140)); // Usa cor acinzentada para destacar menos
        sub.setAlignmentX(LEFT_ALIGNMENT); // Alinha subtítulo à esquerda

        textPanel.add(title); // Pendura título principal
        textPanel.add(sub); // Pendura subtítulo
        p.add(textPanel); // Agrega ambos textos no painel do logotipo

        return p; // Entrega o painel do logotipo e cabeçalho pronto
    } // Fim de buildLogoSection

    private JButton navButton(String label, String card) { // Método encarregado de criar cada botão de navegação
        JButton btn = new JButton(label); // Inicializa a instância do botão
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Prepara a fonte do botão
        btn.setForeground(TEXT_NAV); // Colore o texto base do botão
        btn.setBackground(SIDEBAR_BG); // Dá cor padrão de botão não pressionado
        btn.setBorderPainted(false); // Omite visual de borda do swing
        btn.setFocusPainted(false); // Retira anel em pontilhado quando recebe clique
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Deixa o texto recuado à esquerda do botão
        btn.setAlignmentX(LEFT_ALIGNMENT); // Alinha a esquerda dentro do Layout pai
        btn.setMaximumSize(new Dimension(210, 40)); // Põe teto ao tamanho assumível pelo componente
        btn.setPreferredSize(new Dimension(210, 40)); // Declara dimensão recomendada de funcionamento
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Seta cursor amigável estilo clique web
        btn.setBorder(new EmptyBorder(8, 16, 8, 16)); // Afasta a área interna das extremidades por segurança

        btn.addMouseListener(new MouseAdapter() { // Escuta o trânsito do cursor do mouse em cima do botão
            @Override public void mouseEntered(MouseEvent e) { // Quando a setinha pairar sobre
                if (btn != activeButton) { btn.setBackground(HOVER_BG); btn.setForeground(HOVER_FG); } // Clareia se for inativo, para indicar interatividade
            } // Conclui evento mouseEntered
            @Override public void mouseExited(MouseEvent e) { // Quando a setinha sair fora
                if (btn != activeButton) { btn.setBackground(SIDEBAR_BG); btn.setForeground(TEXT_NAV); } // Regressa ao normal
            } // Conclui evento mouseExited
        }); // Final da declaração do ouvinte de mouse

        btn.addActionListener(e -> { // Lida com pressionar do botão, que leva à transição de painel
            if (activeButton != null) deactivateButton(activeButton); // Cancela o estilo de ativo do botão passado
            activateButton(btn); // Marca esse novo botão como o eleito em negrito e cor azul
            cardLayout.show(contentPanel, card); // Comando final de troca visual do card no centro
            refreshPanel(card); // Sinaliza ao painel aberto a missão de resgatar os próprios dados do banco
        }); // Acaba método da expressão lambda

        return btn; // Retorna este prático botão de menu lateral completamente estilizado e parametrizado
    } // Fim de navButton

    private void activateButton(JButton btn) { // Assinala estilo de destaque contínuo no botão correspondente a tela atual
        activeButton = btn; // Anota referência na variável persistente da classe
        btn.setBackground(ACTIVE_BG); // Pinta de azulão
        btn.setForeground(ACTIVE_FG); // Pinta palavras de branco total
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Engorda a tipografia pra dar visibilidade
    } // Retorna

    private void deactivateButton(JButton btn) { // Método reverso, retira estilo ativado
        btn.setBackground(SIDEBAR_BG); // Recobra o fundo cinzinha ameno
        btn.setForeground(TEXT_NAV); // Bota o texto em tom mais quieto
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Tira o peso pesado da font
    } // Fim de deactivate

    private void refreshPanel(String card) { // Atualiza conteúdo após transição e cliques
        switch (card) { // Executa chave com base no apelido em string mandado
            case "dashboard"     -> dashboardPanel.refresh(); // Dispara método exclusivo do panel para relatórios em chart
            case "produtos"      -> produtosPanel.refresh(); // Pede pro JTable de produtos puxar do DB o atualizado
            case "estoque"       -> lotesPanel.refresh(); // Mesma mecânica pro painel de estoques de lote
            case "historico"     -> descartesPanel.refresh(); // Vai atrás das logs e insersões antigas
            case "configuracoes" -> { // Protegendo um bloco sensível
                try { configuracoesPanel.refresh(); } catch (Exception e) {} // Conserta e camufla perdas decorrentes
            } // Encerra encapsulamento seguro das specs
        } // Encerra chave switch de ramificações
    } // Dá por acabado a missão deste atualizador de informações
}