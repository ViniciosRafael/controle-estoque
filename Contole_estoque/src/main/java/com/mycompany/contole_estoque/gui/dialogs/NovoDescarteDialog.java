package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Diálogo para registrar um novo Descarte de lote.
 * Chama Descarte.registrar() que aplica darBaixa() no lote automaticamente.
 */
public class NovoDescarteDialog extends JDialog {

    private JComboBox<LoteEstoque> cbLote;
    private JTextField             txtQuantidade, txtMotivo;

    public NovoDescarteDialog(JFrame owner) {
        super(owner, "Registrar Descarte", true);
        setSize(480, 310);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 28, 22, 28));

        cbLote = new JComboBox<>();
        cbLote.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (LoteEstoque l : EstoqueStore.get().getLotes()) {
            if (l.getQuantidade() > 0) cbLote.addItem(l);
        }
        cbLote.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean focus) {
                super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof LoteEstoque lt)
                    setText("Lote #" + lt.getIdLote() + "  —  " +
                            lt.getProduto().getNome() + "  (qtd: " + lt.getQuantidade() + ")");
                return this;
            }
        });

        addField(root, "Lote:",        cbLote);
        addField(root, "Quantidade:",  txtQuantidade = new JTextField());
        addField(root, "Motivo:",      txtMotivo     = new JTextField());
        root.add(Box.createVerticalStrut(8));
        root.add(buttonBar());
        setContentPane(root);
    }

    private void addField(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        if (field instanceof JTextField tf) tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(3));
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
    }

    private JPanel buttonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());
        JButton btnReg = new JButton("Registrar");
        btnReg.setBackground(new Color(185, 50, 50));
        btnReg.setForeground(Color.WHITE);
        btnReg.setBorderPainted(false);
        btnReg.addActionListener(e -> registrar());
        bar.add(btnCancel);
        bar.add(btnReg);
        return bar;
    }

    private void registrar() {
        try {
            LoteEstoque lote = (LoteEstoque) cbLote.getSelectedItem();
            if (lote == null) return;
            int    qtd    = Integer.parseInt(txtQuantidade.getText().trim());
            String motivo = txtMotivo.getText().trim();
            if (qtd <= 0 || qtd > lote.getQuantidade()) {
                err("Quantidade inválida (máx: " + lote.getQuantidade() + ")."); return;
            }
            if (motivo.isEmpty()) { err("Informe o motivo do descarte."); return; }

            int id = EstoqueStore.get().nextId();
            Descarte d = new Descarte(id, lote, qtd, LocalDate.now(), motivo);
            d.registrar();   // aplica darBaixa no lote automaticamente
            EstoqueStore.get().getDescartes().add(d);
            dispose();
        } catch (NumberFormatException ex) { err("Quantidade inválida."); }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
