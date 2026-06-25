package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.dialogs.NovoProdutoDialog;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Produtos — exibe perecíveis e não-perecíveis em abas separadas.
 * Permite criar e remover produtos.
 */
public class ProdutosPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultTableModel perecModel, naoModel;
    private JTable            perecTable, naoTable;

    public ProdutosPanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Produtos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        p.add(title, BorderLayout.WEST);

        JButton btnNovo = actionButton("+ Novo Produto", new Color(16, 163, 127));
        btnNovo.addActionListener(e -> {
            new NovoProdutoDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            refresh();
        });
        p.add(btnNovo, BorderLayout.EAST);
        return p;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // ── Perecíveis
        String[] pCols = {"ID","Nome","Categoria","Preço Unit.","Data Validade","Est. Mínimo","Status"};
        perecModel = emptyModel(pCols);
        perecTable = buildTable(perecModel);
        perecTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        perecTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        perecTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        setStatusRenderer(perecTable, 6);

        tabs.addTab("🥩  Perecíveis", tablePanel(perecTable, perecModel, true));

        // ── Não Perecíveis
        String[] nCols = {"ID","Nome","Categoria","Preço Unitário","Est. Mínimo"};
        naoModel = emptyModel(nCols);
        naoTable = buildTable(naoModel);
        naoTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        naoTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        naoTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        naoTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        tabs.addTab("🥫  Não Perecíveis", tablePanel(naoTable, naoModel, false));
        return tabs;
    }

    private JPanel tablePanel(JTable table, DefaultTableModel model, boolean isPerec) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        JButton btnRem = actionButton("🗑  Remover", new Color(185, 50, 50));
        btnRem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { warn("Selecione um produto."); return; }
            if (JOptionPane.showConfirmDialog(this, "Remover produto selecionado?",
                    "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int id = (int) model.getValueAt(row, 0);
                if (isPerec) EstoqueStore.get().getPerec().removeIf(pp -> pp.getId() == id);
                else         EstoqueStore.get().getNaoPerec().removeIf(pp -> pp.getId() == id);
                refresh();
            }
        });
        bar.add(btnRem);
        p.add(bar, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        perecModel.setRowCount(0);
        for (ProdutoPerecivel p : EstoqueStore.get().getPerec()) {
            int dias   = p.diasParaVencer();
            String st  = p.isVencido() ? "⛔ Vencido" : dias <= 5 ? "⚠ Próx. Venc." : "✅ OK";
            perecModel.addRow(new Object[]{
                p.getId(), p.getNome(), p.getCategoria(),
                String.format("R$ %.2f", p.getPrecoUnitario()),
                p.getDataValidade().format(FMT), p.getEstoqueMinimo(), st
            });
        }
        naoModel.setRowCount(0);
        for (ProdutoNaoPerecivel p : EstoqueStore.get().getNaoPerec()) {
            naoModel.addRow(new Object[]{
                p.getId(), p.getNome(), p.getCategoria(),
                String.format("R$ %.2f", p.getPrecoUnitario()),
                p.getEstoqueMinimo()
            });
        }
    }

    // ----------------------------------------------------------------- helpers
    private DefaultTableModel emptyModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        DashboardPanel.styleTable(t);
        return t;
    }

    /** Colorizes the status column. */
    private void setStatusRenderer(JTable table, int col) {
        table.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, focus, row, c);
                if (!sel && v != null) {
                    String s = v.toString();
                    if (s.contains("Vencido"))      comp.setForeground(new Color(255,  80,  80));
                    else if (s.contains("Próx"))    comp.setForeground(new Color(255, 175,   0));
                    else                             comp.setForeground(new Color( 60, 210, 110));
                }
                return comp;
            }
        });
    }

    static JButton actionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        return btn;
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
