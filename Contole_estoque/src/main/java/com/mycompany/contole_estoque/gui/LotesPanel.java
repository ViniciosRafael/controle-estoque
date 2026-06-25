package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.dialogs.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Lotes — lista lotes com linhas coloridas por status de vencimento.
 * Permite criar novos lotes e dar baixa de quantidade.
 */
public class LotesPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultTableModel model;
    private JTable            table;

    public LotesPanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(),  BorderLayout.CENTER);
        add(buildLegend(), BorderLayout.SOUTH);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Lotes de Estoque");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        p.add(title, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);

        JButton btnBaixa = ProdutosPanel.actionButton("📉  Dar Baixa", new Color(200, 125, 0));
        btnBaixa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { warn("Selecione um lote."); return; }
            LoteEstoque lote = EstoqueStore.get().getLotes().get(row);
            new DarBaixaDialog((JFrame) SwingUtilities.getWindowAncestor(this), lote).setVisible(true);
            refresh();
        });

        JButton btnNovo = ProdutosPanel.actionButton("+ Novo Lote", new Color(0, 120, 210));
        btnNovo.addActionListener(e -> {
            if (EstoqueStore.get().getPerec().isEmpty()) {
                warn("Cadastre pelo menos um produto perecível primeiro.");
                return;
            }
            new NovoLoteDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            refresh();
        });

        btns.add(btnBaixa);
        btns.add(btnNovo);
        p.add(btns, BorderLayout.EAST);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"Lote #","Produto","Qtd Atual","Est. Mín.","Data Entrada","Dias p/ Vencer","Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        DashboardPanel.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(65);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(6).setPreferredWidth(135);

        // colorize all cells based on status column
        TableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    String st = (String) model.getValueAt(row, 6);
                    if (st != null && st.contains("Vencido"))  c.setForeground(new Color(255, 80, 80));
                    else if (st != null && st.contains("Próx")) c.setForeground(new Color(255, 175, 0));
                    else                                        c.setForeground(new Color(60, 210, 110));
                }
                return c;
            }
        };
        for (int i = 0; i < cols.length; i++) table.getColumnModel().getColumn(i).setCellRenderer(colorRenderer);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }

    private JPanel buildLegend() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        p.setOpaque(false);
        legend(p, "✅  OK",                          new Color( 60, 210, 110));
        legend(p, "⚠  Próximo ao Vencimento (≤5 d)", new Color(255, 175,   0));
        legend(p, "⛔  Vencido",                     new Color(255,  80,  80));
        return p;
    }

    private void legend(JPanel p, String text, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(c);
        p.add(l);
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        model.setRowCount(0);
        for (LoteEstoque lote : EstoqueStore.get().getLotes()) {
            int dias   = lote.diasParaVencer();
            String st  = lote.isVencido() ? "⛔ Vencido"
                       : dias <= 5        ? "⚠ Próx. Venc."
                                          : "✅ OK";
            model.addRow(new Object[]{
                lote.getIdLote(),
                lote.getProduto().getNome(),
                lote.getQuantidade(),
                lote.getProduto().getEstoqueMinimo(),
                lote.getDataEntrada().format(FMT),
                lote.isVencido() ? "VENCIDO" : dias + " dia(s)",
                st
            });
        }
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
