package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Esta tela é o "Resumo Geral" (Dashboard).
 * Ela mostra cards com números totais e uma tabela de alertas.
 */
public class DashboardPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Cores pegas da classe Tema para manter o padrão visual
    private static final Color BG          = Tema.FUNDO;
    private static final Color CARD_BG     = Tema.CARD_BG;
    private static final Color BORDER_CLR  = Tema.BORDA;
    private static final Color TEXT_TITLE  = Tema.TEXTO_TITULO;
    private static final Color TEXT_SUB    = Tema.TEXTO_SUB;

    // Elementos de texto que mudam conforme os dados do sistema
    private JLabel            lblProdutos, lblLotes, lblAlertas, lblPrejuizo;
    private DefaultTableModel alertasModel;
    private JComboBox<String> cbFiltro;

    public DashboardPanel() {
        setBackground(BG);
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        refresh(); // Carrega os dados iniciais
    }

    // Cria o título da tela
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_TITLE);
        p.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("Visão geral do sistema");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_SUB);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    // Monta o meio da tela com os cards e a tabela de alertas
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 24));
        center.setOpaque(false);
        center.add(buildCards(),          BorderLayout.NORTH);
        center.add(buildAlertsSection(),  BorderLayout.CENTER);
        return center;
    }

    // Cria a fileira de cards (Total de Produtos, Entradas, Alertas, Prejuízo)
    private JPanel buildCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);

        lblProdutos = addCard(row, "Total de Produtos",   "0",        new Color(0,  120, 210));
        lblLotes    = addCard(row, "Entradas de Estoque", "0",        new Color(16, 163, 127));
        lblAlertas  = addCard(row, "Alertas Ativos",      "0",        new Color(210, 120,  0));
        lblPrejuizo = addCard(row, "Prejuízo Acumulado",  "R$ 0,00",  new Color(200,  50,  60));
        return row;
    }

    // Desenha um card individual com título, valor e uma cor de destaque
    private JLabel addCard(JPanel parent, String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel lAccent = new JLabel();
        lAccent.setPreferredSize(new Dimension(4, 1));
        lAccent.setOpaque(true);
        lAccent.setBackground(accent);

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lTitle.setForeground(TEXT_SUB);

        JLabel lValue = new JLabel(value);
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lValue.setForeground(accent);

        card.add(lAccent, BorderLayout.WEST);
        card.add(lTitle,  BorderLayout.NORTH);
        card.add(lValue,  BorderLayout.CENTER);
        parent.add(card);
        return lValue;
    }

    // Cria a seção que contém a tabela de alertas e o filtro
    private JPanel buildAlertsSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel heading = new JLabel("Alertas do Sistema");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setForeground(TEXT_TITLE);
        header.add(heading, BorderLayout.WEST);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        ctrl.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFiltro.setForeground(TEXT_SUB);

        // Opções para filtrar os alertas por tipo
        cbFiltro = new JComboBox<>(new String[]{"Todos", "VENCIMENTO", "ESTOQUE_MINIMO"});
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFiltro.addActionListener(e -> refreshAlertas());

        JButton btnGerar = ProdutosPanel.actionButton("Atualizar Alertas", Tema.PRIMARIA);
        btnGerar.addActionListener(e -> { EstoqueStore.get().gerarAlertas(); refresh(); });

        ctrl.add(lblFiltro);
        ctrl.add(cbFiltro);
        ctrl.add(btnGerar);
        header.add(ctrl, BorderLayout.EAST);

        section.add(header,            BorderLayout.NORTH);
        section.add(buildAlertsTable(),BorderLayout.CENTER);
        return section;
    }

    // Configura a tabela onde os alertas aparecem
    private JScrollPane buildAlertsTable() {
        String[] cols = {"ID", "Tipo", "Mensagem", "Data"};
        alertasModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(alertasModel);
        styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(600);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Define as cores das linhas (Vermelho para crítico, Laranja para vencimento)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(Tema.FUNDO);
                    String tipo = (String) alertasModel.getValueAt(row, 1);
                    c.setForeground("VENCIMENTO".equals(tipo)
                        ? Tema.ALERTA_TXT : Tema.CRITICO_TXT);
                } else {
                    c.setBackground(new Color(0, 120, 210));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER_CLR, 1, true));
        sp.getViewport().setBackground(Tema.FUNDO);
        return sp;
    }

    // Atualiza todos os números e a tabela com os dados mais recentes da memória
    public void refresh() {
        EstoqueStore s = EstoqueStore.get();
        s.limparLotesZerados();
        s.gerarAlertas(); // Manda o sistema recalcular os alertas
        lblProdutos.setText(String.valueOf(s.getPerec().size() + s.getNaoPerec().size()));
        lblLotes.setText(String.valueOf(s.getLotes().size()));
        lblAlertas.setText(String.valueOf(s.getAlertas().size()));
        double prej = s.getDescartes().stream().mapToDouble(Descarte::calcularPrejuizo).sum();
        lblPrejuizo.setText(String.format("R$ %.2f", prej).replace('.', ','));
        refreshAlertas();
    }

    // Atualiza apenas a tabela de alertas conforme o filtro selecionado
    private void refreshAlertas() {
        alertasModel.setRowCount(0);
        String filtro = (String) cbFiltro.getSelectedItem();
        for (Alerta a : EstoqueStore.get().getAlertas()) {
            if ("Todos".equals(filtro) || a.getTipo().equals(filtro)) {
                alertasModel.addRow(new Object[]{
                    a.getAlertaId(), a.getTipo(), a.getMensagem(), a.getData().format(FMT)
                });
            }
        }
    }

    // Método utilitário para deixar as tabelas do sistema bonitas e padronizadas
    public static void styleTable(JTable table) {
        table.setRowHeight(38);
        table.setBackground(Tema.FUNDO);
        table.setForeground(Tema.TEXTO_TITULO);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(0, 120, 210));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(Tema.BORDA);
        table.setIntercellSpacing(new Dimension(0, 1));
        
        JTableHeader h = table.getTableHeader();
        h.setPreferredSize(new Dimension(0, 40));
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setBackground(Tema.HEADER_BG);
        h.setForeground(Tema.TEXTO_TITULO);
        h.setBorder(new MatteBorder(0, 0, 1, 0, Tema.BORDA));
    }
}