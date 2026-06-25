package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Painel do Dashboard.
 * Exibe cards de resumo e, abaixo, a tabela completa de alertas
 * (vencimento e estoque mínimo) com filtro e botão de atualização.
 * Não existe mais um painel separado de Alertas.
 */
public class DashboardPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JLabel           lblProdutos, lblLotes, lblAlertas, lblPrejuizo;
    private DefaultTableModel alertasModel;
    private JComboBox<String> cbFiltro;

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        p.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("Visão geral do sistema");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(130, 130, 155));
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 24));
        center.setOpaque(false);
        center.add(buildCards(),       BorderLayout.NORTH);
        center.add(buildAlertsSection(), BorderLayout.CENTER);
        return center;
    }

    // ── metric cards ────────────────────────────────────────────────────────
    private JPanel buildCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);

        lblProdutos = addCard(row, "Total de Produtos",  "0",       new Color(0,  120, 215));
        lblLotes    = addCard(row, "Entradas de Estoque","0",       new Color(16, 163, 127));
        lblAlertas  = addCard(row, "Alertas Ativos",     "0",       new Color(210, 130,  0));
        lblPrejuizo = addCard(row, "Prejuizo Acumulado", "R$ 0,00", new Color(200,  50,  60));
        return row;
    }

    private JLabel addCard(JPanel parent, String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 5, getHeight(), 4, 4);
            }
        };
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60), 1, true),
            new EmptyBorder(18, 24, 18, 24)
        ));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lTitle.setForeground(new Color(140, 140, 160));

        JLabel lValue = new JLabel(value);
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lValue.setForeground(Color.WHITE);

        card.add(lTitle, BorderLayout.NORTH);
        card.add(lValue, BorderLayout.CENTER);
        parent.add(card);
        return lValue;
    }

    // ── seção de alertas ────────────────────────────────────────────────────
    private JPanel buildAlertsSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(false);

        // cabeçalho da seção
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel heading = new JLabel("Alertas do Sistema");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.add(heading, BorderLayout.WEST);

        // controles (filtro + botão atualizar)
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        ctrl.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cbFiltro = new JComboBox<>(new String[]{"Todos", "VENCIMENTO", "ESTOQUE_MINIMO"});
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltro.addActionListener(e -> refreshAlertas());

        JButton btnGerar = ProdutosPanel.actionButton("Atualizar Alertas", new Color(0, 120, 210));
        btnGerar.addActionListener(e -> {
            EstoqueStore.get().gerarAlertas();
            refresh();
        });

        ctrl.add(lblFiltro);
        ctrl.add(cbFiltro);
        ctrl.add(btnGerar);
        header.add(ctrl, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);
        section.add(buildAlertsTable(), BorderLayout.CENTER);
        return section;
    }

    private JScrollPane buildAlertsTable() {
        String[] cols = {"ID", "Tipo", "Mensagem", "Data"};
        alertasModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(alertasModel);
        styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(155);
        table.getColumnModel().getColumn(2).setPreferredWidth(600);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    String tipo = (String) alertasModel.getValueAt(row, 1);
                    if ("VENCIMENTO".equals(tipo))
                        c.setForeground(new Color(255, 170, 0));
                    else
                        c.setForeground(new Color(255, 90, 90));
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        EstoqueStore s = EstoqueStore.get();
        s.gerarAlertas();

        lblProdutos.setText(String.valueOf(s.getPerec().size() + s.getNaoPerec().size()));
        lblLotes.setText(String.valueOf(s.getLotes().size()));
        lblAlertas.setText(String.valueOf(s.getAlertas().size()));

        double prej = s.getDescartes().stream().mapToDouble(Descarte::calcularPrejuizo).sum();
        lblPrejuizo.setText(String.format("R$ %.2f", prej).replace('.', ','));

        refreshAlertas();
    }

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

    // ----------------------------------------------------------------- helper
    static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
    }
}
