package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Alertas — lista alertas ativos com filtro por tipo.
 * Botão para regerar alertas com base no estado atual dos lotes.
 */
public class AlertasPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultTableModel model;
    private JComboBox<String> cbFiltro;

    public AlertasPanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(),  BorderLayout.CENTER);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Alertas do Sistema");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        p.add(title, BorderLayout.WEST);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        ctrl.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltro = new JComboBox<>(new String[]{"Todos","VENCIMENTO","ESTOQUE_MINIMO"});
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltro.addActionListener(e -> refresh());

        JButton btnGerar = ProdutosPanel.actionButton("🔄  Gerar Alertas", new Color(0, 120, 210));
        btnGerar.addActionListener(e -> {
            EstoqueStore.get().gerarAlertas();
            refresh();
            JOptionPane.showMessageDialog(this,
                EstoqueStore.get().getAlertas().size() + " alerta(s) gerado(s).",
                "Alertas Atualizados", JOptionPane.INFORMATION_MESSAGE);
        });

        ctrl.add(lblFiltro);
        ctrl.add(cbFiltro);
        ctrl.add(btnGerar);
        p.add(ctrl, BorderLayout.EAST);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"ID","Tipo","Mensagem","Data"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        DashboardPanel.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(560);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                if (!sel) {
                    String tipo = (String) model.getValueAt(r, 1);
                    comp.setForeground("VENCIMENTO".equals(tipo)
                        ? new Color(255, 170, 0) : new Color(255, 90, 90));
                }
                return comp;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        model.setRowCount(0);
        String filtro = (String) cbFiltro.getSelectedItem();
        for (Alerta a : EstoqueStore.get().getAlertas()) {
            if ("Todos".equals(filtro) || a.getTipo().equals(filtro)) {
                model.addRow(new Object[]{
                    a.getAlertaId(), a.getTipo(), a.getMensagem(), a.getData().format(FMT)
                });
            }
        }
    }
}