package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.dialogs.NovoDescarteDialog;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Descartes — histórico de descartes com total de prejuízo.
 */
public class DescartesPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultTableModel model;
    private JLabel            lblTotal;

    public DescartesPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(),  BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Registro de Descartes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(20, 22, 35));
        p.add(title, BorderLayout.WEST);

        JButton btnNovo = ProdutosPanel.actionButton("+ Novo Descarte", new Color(185, 50, 50));
        btnNovo.addActionListener(e -> {
            if (EstoqueStore.get().getLotes().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum lote disponível.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new NovoDescarteDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            refresh();
        });
        p.add(btnNovo, BorderLayout.EAST);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"ID","Produto","Lote #","Quantidade","Data Descarte","Motivo","Prejuízo"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        DashboardPanel.styleTable(table);
        table.getColumnModel().getColumn(1).setPreferredWidth(210);
        table.getColumnModel().getColumn(5).setPreferredWidth(220);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);

        // highlight prejuízo column in red
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                if (!sel) comp.setForeground(new Color(255, 90, 90));
                return comp;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        p.setOpaque(false);
        lblTotal = new JLabel("Prejuízo Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotal.setForeground(new Color(255, 90, 90));
        p.add(lblTotal);
        return p;
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        model.setRowCount(0);
        double total = 0;
        for (Descarte d : EstoqueStore.get().getDescartes()) {
            double prej = d.calcularPrejuizo();
            total += prej;
            model.addRow(new Object[]{
                d.getDescarteId(),
                d.getLote().getProduto().getNome(),
                d.getLote().getIdLote(),
                d.getQuantidadeDescartada(),
                d.getDataDescarte().format(FMT),
                d.getMotivo(),
                String.format("R$ %.2f", prej)
            });
        }
        lblTotal.setText("Prejuízo Total: " + String.format("R$ %.2f", total).replace('.', ','));
    }
}
