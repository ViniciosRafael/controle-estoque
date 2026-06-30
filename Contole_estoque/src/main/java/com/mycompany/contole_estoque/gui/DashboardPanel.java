package com.mycompany.contole_estoque.gui; // Define o pacote do painel de dashboard

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

// Esta tela é o "Resumo Geral" (Dashboard).
// Ela mostra cards com números totais e uma tabela de alertas.
public class DashboardPanel extends JPanel { // Declara a classe do painel principal que herda de JPanel

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Define o formatador de datas padrão

    private static final Color BG          = Tema.FUNDO; // Define a cor de fundo usando a cor do tema
    private static final Color CARD_BG     = Tema.CARD_BG; // Define a cor de fundo dos cards baseada no tema
    private static final Color BORDER_CLR  = Tema.BORDA; // Define a cor das bordas baseada no tema
    private static final Color TEXT_TITLE  = Tema.TEXTO_TITULO; // Define a cor dos títulos usando o tema
    private static final Color TEXT_SUB    = Tema.TEXTO_SUB; // Define a cor dos subtítulos pelo tema

    private JLabel            lblProdutos, lblLotes, lblAlertas, lblPrejuizo; // Declara os rótulos de texto para os cards
    private DefaultTableModel alertasModel; // Declara o modelo de dados para a tabela de alertas
    private JComboBox<String> cbFiltro; // Declara o combo box para filtragem

    public DashboardPanel() { // Construtor da classe DashboardPanel
        setBackground(BG); // Define a cor de fundo do painel
        setLayout(new BorderLayout(0, 20)); // Configura o layout do painel com bordas e espaçamento
        setBorder(new EmptyBorder(28, 28, 28, 28)); // Adiciona uma margem interna de 28 pixels
        add(buildHeader(),  BorderLayout.NORTH); // Adiciona o cabeçalho no topo da tela
        add(buildCenter(),  BorderLayout.CENTER); // Adiciona o centro da tela com cards e alertas
        refresh(); // Carrega os dados iniciais do sistema
    } // Fim do construtor DashboardPanel

    private JPanel buildHeader() { // Método que constrói o cabeçalho da tela
        JPanel p = new JPanel(new BorderLayout()); // Cria um novo painel com layout de borda
        p.setOpaque(false); // Torna o painel transparente para usar a cor de fundo do pai

        JLabel title = new JLabel("Dashboard"); // Cria o rótulo com o título da tela
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Define a fonte e tamanho do título
        title.setForeground(TEXT_TITLE); // Define a cor do título
        p.add(title, BorderLayout.WEST); // Adiciona o título à esquerda do cabeçalho

        JLabel sub = new JLabel("Visão geral do sistema"); // Cria o subtítulo da tela
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Define a fonte e o tamanho do subtítulo
        sub.setForeground(TEXT_SUB); // Define a cor do subtítulo
        p.add(sub, BorderLayout.SOUTH); // Adiciona o subtítulo abaixo do título
        return p; // Retorna o painel de cabeçalho montado
    } // Fim do método buildHeader

    private JPanel buildCenter() { // Método que monta o meio da tela
        JPanel center = new JPanel(new BorderLayout(0, 24)); // Cria um painel central com layout de borda
        center.setOpaque(false); // Torna o painel transparente
        center.add(buildCards(),          BorderLayout.NORTH); // Adiciona a área de cards na parte superior
        center.add(buildAlertsSection(),  BorderLayout.CENTER); // Adiciona a seção de alertas no centro
        return center; // Retorna o painel central montado
    } // Fim do método buildCenter

    private JPanel buildCards() { // Método que cria a fileira de cards
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0)); // Cria um painel com grade de 1 linha e 4 colunas
        row.setOpaque(false); // Torna o painel de cards transparente

        lblProdutos = addCard(row, "Total de Produtos",   "0",        new Color(0,  120, 210)); // Adiciona o card de produtos
        lblLotes    = addCard(row, "Entradas de Estoque", "0",        new Color(16, 163, 127)); // Adiciona o card de entradas
        lblAlertas  = addCard(row, "Alertas Ativos",      "0",        new Color(210, 120,  0)); // Adiciona o card de alertas
        lblPrejuizo = addCard(row, "Prejuízo Acumulado",  "R$ 0,00",  new Color(200,  50,  60)); // Adiciona o card de prejuízo
        return row; // Retorna a fileira de cards montada
    } // Fim do método buildCards

    private JLabel addCard(JPanel parent, String title, String value, Color accent) { // Método para desenhar um card individual
        JPanel card = new JPanel(new BorderLayout(0, 8)); // Cria o painel do card com layout e espaçamento
        card.setBackground(CARD_BG); // Define a cor de fundo do card
        card.setBorder(new CompoundBorder( // Aplica múltiplas bordas ao card
            new LineBorder(BORDER_CLR, 1, true), // Borda externa arredondada e colorida
            new EmptyBorder(16, 20, 16, 20) // Espaçamento interno do card
        )); // Fim da configuração da borda

        JLabel lAccent = new JLabel(); // Cria um rótulo para atuar como barra de destaque
        lAccent.setPreferredSize(new Dimension(4, 1)); // Define a largura da barra lateral
        lAccent.setOpaque(true); // Torna o rótulo visível
        lAccent.setBackground(accent); // Define a cor da barra de destaque

        JLabel lTitle = new JLabel(title); // Cria o título do card
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Configura a fonte do título do card
        lTitle.setForeground(TEXT_SUB); // Define a cor de texto secundária

        JLabel lValue = new JLabel(value); // Cria o rótulo com o valor do card
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Configura a fonte e o tamanho do valor
        lValue.setForeground(accent); // Define a cor de destaque para o valor

        card.add(lAccent, BorderLayout.WEST); // Adiciona a barra na esquerda do card
        card.add(lTitle,  BorderLayout.NORTH); // Adiciona o título no topo do card
        card.add(lValue,  BorderLayout.CENTER); // Adiciona o valor no centro do card
        parent.add(card); // Adiciona o card criado ao painel pai
        return lValue; // Retorna o componente do valor para atualização futura
    } // Fim do método addCard

    private JPanel buildAlertsSection() { // Método que cria a área da tabela e filtros de alertas
        JPanel section = new JPanel(new BorderLayout(0, 10)); // Painel principal da seção com espaçamento
        section.setOpaque(false); // Torna o painel da seção transparente

        JPanel header = new JPanel(new BorderLayout()); // Cria o painel de cabeçalho da seção
        header.setOpaque(false); // Torna o cabeçalho transparente

        JLabel heading = new JLabel("Alertas do Sistema"); // Título da seção de alertas
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Define a fonte do título da seção
        heading.setForeground(TEXT_TITLE); // Define a cor do título
        header.add(heading, BorderLayout.WEST); // Posiciona o título à esquerda

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // Painel de controle alinhado à direita
        ctrl.setOpaque(false); // Torna o controle transparente

        JLabel lblFiltro = new JLabel("Filtrar:"); // Cria o rótulo do filtro
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Define a fonte do rótulo
        lblFiltro.setForeground(TEXT_SUB); // Define a cor do rótulo

        cbFiltro = new JComboBox<>(new String[]{"Todos", "VENCIMENTO", "ESTOQUE_MINIMO"}); // Instancia o combo box com as opções
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Define a fonte do combo box
        cbFiltro.addActionListener(e -> refreshAlertas()); // Adiciona ouvinte para atualizar a tabela ao mudar filtro

        ctrl.add(lblFiltro); // Adiciona o rótulo ao painel de controle
        ctrl.add(cbFiltro); // Adiciona o combo box ao painel de controle
        header.add(ctrl, BorderLayout.EAST); // Adiciona o painel de controle à direita do cabeçalho

        section.add(header,            BorderLayout.NORTH); // Adiciona o cabeçalho no topo da seção
        section.add(buildAlertsTable(),BorderLayout.CENTER); // Adiciona a tabela de alertas no centro
        return section; // Retorna a seção de alertas pronta
    } // Fim do método buildAlertsSection

    private JScrollPane buildAlertsTable() { // Método que constrói a tabela com as notificações
        String[] cols = {"ID", "Tipo", "Mensagem", "Data"}; // Define as colunas da tabela
        alertasModel = new DefaultTableModel(cols, 0) { // Inicializa o modelo da tabela sem linhas
            @Override public boolean isCellEditable(int r, int c) { return false; } // Impede a edição das células
        }; // Fim da declaração do DefaultTableModel

        JTable table = new JTable(alertasModel); // Cria a tabela de alertas vinculada ao modelo
        styleTable(table); // Aplica a estilização na tabela
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // Define a largura da coluna ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Define a largura da coluna Tipo
        table.getColumnModel().getColumn(2).setPreferredWidth(600); // Define a largura da coluna Mensagem
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Define a largura da coluna Data

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() { // Configura a renderização customizada das células
            @Override public Component getTableCellRendererComponent( // Método principal de renderização de célula
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) { // Assinatura com atributos da célula
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col); // Chama a renderização padrão
                if (!sel) { // Verifica se a linha não está selecionada
                    c.setBackground(Tema.FUNDO); // Aplica a cor de fundo padrão nas linhas
                    String tipo = (String) alertasModel.getValueAt(row, 1); // Lê o tipo do alerta na coluna 1
                    c.setForeground("VENCIMENTO".equals(tipo) // Condição de formatação baseada no tipo
                        ? Tema.ALERTA_TXT : Tema.CRITICO_TXT); // Define a cor do texto para alertas
                } else { // Caso a linha esteja selecionada
                    c.setBackground(new Color(0, 120, 210)); // Cor de fundo para a seleção
                    c.setForeground(Color.WHITE); // Cor branca para o texto selecionado
                } // Fim do if-else de seleção
                return c; // Retorna o componente formatado
            } // Fim do método getTableCellRendererComponent
        }); // Fim do setDefaultRenderer

        JScrollPane sp = new JScrollPane(table); // Adiciona barra de rolagem na tabela
        sp.setBorder(new LineBorder(BORDER_CLR, 1, true)); // Define a borda do painel de rolagem
        sp.getViewport().setBackground(Tema.FUNDO); // Configura o fundo da área visível da tabela
        return sp; // Retorna o JScrollPane criado
    } // Fim do método buildAlertsTable

    public void refresh() { // Método para atualizar os dados visuais
        EstoqueStore s = EstoqueStore.get(); // Obtém a instância da loja de dados do estoque
        s.limparLotesZerados(); // Remove os lotes sem quantidade
        s.gerarAlertas(); // Recalcula as situações de alerta
        lblProdutos.setText(String.valueOf(s.getPerec().size() + s.getNaoPerec().size())); // Atualiza total de produtos
        lblLotes.setText(String.valueOf(s.getLotes().size())); // Atualiza a quantidade de entradas de lote
        lblAlertas.setText(String.valueOf(s.getAlertas().size())); // Exibe a contagem de alertas ativos
        double prej = s.getDescartes().stream().mapToDouble(Descarte::calcularPrejuizo).sum(); // Calcula o total de prejuízos
        lblPrejuizo.setText(String.format("R$ %.2f", prej).replace('.', ',')); // Exibe o valor do prejuízo formatado
        refreshAlertas(); // Atualiza a visualização dos dados na tabela
    } // Fim do método refresh

    private void refreshAlertas() { // Filtra as informações a serem exibidas na tabela
        alertasModel.setRowCount(0); // Limpa as linhas atuais da tabela
        String filtro = (String) cbFiltro.getSelectedItem(); // Obtém o item de filtro escolhido no combo box
        for (Alerta a : EstoqueStore.get().getAlertas()) { // Percorre todos os alertas do sistema
            if ("Todos".equals(filtro) || a.getTipo().equals(filtro)) { // Aplica a filtragem pelo tipo
                alertasModel.addRow(new Object[]{ // Adiciona uma nova linha com os dados do alerta
                    a.getAlertaId(), a.getTipo(), a.getMensagem(), a.getData().format(FMT) // Valores das colunas ID, Tipo, Msg e Data
                }); // Fim da chamada para adicionar linha
            } // Fim da verificação do filtro
        } // Fim do laço for
    } // Fim do método refreshAlertas

    public static void styleTable(JTable table) { // Ajusta a aparência de tabelas de modo padronizado
        table.setRowHeight(38); // Define a altura de cada linha da tabela
        table.setBackground(Tema.FUNDO); // Configura a cor do fundo
        table.setForeground(Tema.TEXTO_TITULO); // Configura a cor principal das fontes
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Aplica a fonte padrão de dados
        table.setSelectionBackground(new Color(0, 120, 210)); // Modifica o fundo do item selecionado
        table.setSelectionForeground(Color.WHITE); // Garante que a fonte do item em foco fique branca
        table.setShowGrid(true); // Exibe as grades da tabela
        table.setGridColor(Tema.BORDA); // Aplica a cor definida no tema para as grades
        table.setIntercellSpacing(new Dimension(0, 1)); // Ajusta a distância entre células
        
        JTableHeader h = table.getTableHeader(); // Pega a referência do cabeçalho da tabela
        h.setPreferredSize(new Dimension(0, 40)); // Dimensiona a altura do cabeçalho
        h.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Configura fonte destacada
        h.setBackground(Tema.HEADER_BG); // Pinta o fundo da região de cabeçalhos
        h.setForeground(Tema.TEXTO_TITULO); // Modifica a coloração das palavras
        h.setBorder(new MatteBorder(0, 0, 1, 0, Tema.BORDA)); // Sublinha os títulos da tabela
    } // Fim do método styleTable
} ;
    