package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.dialogs.NovoDescarteDialog;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Histórico de Movimentações — exibe todas as movimentações do estoque:
 * inclusões, baixas e descartes. Substitui o antigo painel de "Registro de Descartes".
 *
 * Também exibe o total de prejuízo acumulado (calculado apenas sobre os descartes).
 */
public class DescartesPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultTableModel model;
    private JLabel            lblTotal;
    private JComboBox<String> cbFiltro;

    public DescartesPanel() {
        setBackground(Tema.FUNDO);
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

        JLabel title = new JLabel("Histórico de Movimentações");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Tema.TEXTO_TITULO);
        p.add(title, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFiltro.setForeground(Tema.TEXTO_SUB);
        controls.add(lblFiltro);

        cbFiltro = new JComboBox<>(new String[]{"Todos", "Inclusão", "Baixa", "Descarte"});
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFiltro.addActionListener(e -> refresh());
        controls.add(cbFiltro);

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
        controls.add(btnNovo);

        p.add(controls, BorderLayout.EAST);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"ID", "Tipo", "Produto", "Lote", "Quantidade", "Data", "Observação / Motivo", "Prejuízo"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        DashboardPanel.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(190);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(220);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        // Coloriza as linhas conforme o tipo de movimentação
        TableCellRenderer tipoRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(Tema.FUNDO);
                    int modelRow = t.convertRowIndexToModel(row);
                    String tipo = (String) model.getValueAt(modelRow, 1);
                    if ("Descarte".equals(tipo))
                        c.setForeground(Tema.CRITICO_TXT);
                    else if ("Baixa".equals(tipo))
                        c.setForeground(Tema.ALERTA_TXT);
                    else
                        c.setForeground(Tema.PRIMARIA_TXT);
                } else {
                    c.setBackground(new Color(0, 120, 210));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        for (int i = 0; i < cols.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(tipoRenderer);

        // Permite ordenação clicando no cabeçalho
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Tema.FUNDO);
        return sp;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        p.setOpaque(false);
        lblTotal = new JLabel("Prejuízo Total (descartes): R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotal.setForeground(Tema.CRITICO_TXT);
        p.add(lblTotal);
        return p;
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        model.setRowCount(0);
        String filtro = cbFiltro != null ? (String) cbFiltro.getSelectedItem() : "Todos";

        // Calcula o prejuízo total de todos os descartes (independente do filtro)
        double totalPrejuizo = EstoqueStore.get().getDescartes().stream()
                .mapToDouble(Descarte::calcularPrejuizo).sum();

        for (Movimentacao mov : EstoqueStore.get().getMovimentacoes()) {
            String tipoStr = mov.getTipo().getDescricao();

            // Aplica filtro de tipo
            if (!"Todos".equals(filtro) && !filtro.equals(tipoStr)) continue;

            // Calcula prejuízo apenas para descartes
            String prejStr = "—";
            if (mov.getTipo() == Movimentacao.Tipo.DESCARTE) {
                // Busca o descarte correspondente para calcular o prejuízo real
                double prej = EstoqueStore.get().getDescartes().stream()
                        .filter(d -> d.getLote() != null && mov.getLote() != null
                                && d.getLote().getIdLote() == mov.getLote().getIdLote()
                                && d.getDataDescarte().equals(mov.getData())
                                && d.getQuantidadeDescartada() == mov.getQuantidade())
                        .mapToDouble(Descarte::calcularPrejuizo)
                        .findFirst()
                        .orElse(0.0);
                if (prej > 0) {
                    prejStr = String.format("R$ %.2f", prej).replace('.', ',');
                }
            }

            model.addRow(new Object[]{
                mov.getId(),
                tipoStr,
                mov.getNomeProduto() != null ? mov.getNomeProduto().toUpperCase() : "—",
                mov.getNumeroLote(),
                mov.getQuantidade(),
                mov.getData() != null ? mov.getData().format(FMT) : "—",
                mov.getObservacao() != null ? mov.getObservacao() : "—",
                prejStr
            });
        }

        if (lblTotal != null) {
            lblTotal.setText("Prejuízo Total (descartes): "
                    + String.format("R$ %.2f", totalPrejuizo).replace('.', ','));
        }
    }
}
