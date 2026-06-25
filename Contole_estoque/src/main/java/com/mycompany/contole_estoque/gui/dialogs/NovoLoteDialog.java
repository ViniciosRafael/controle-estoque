package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Diálogo para registrar um novo Lote (vinculado a um produto perecível existente).
 */
public class NovoLoteDialog extends JDialog {

    private JComboBox<ProdutoPerecivel> cbProduto;
    private JTextField                  txtQuantidade;

    public NovoLoteDialog(JFrame owner) {
        super(owner, "Novo Lote de Estoque", true);
        setSize(430, 250);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 28, 22, 28));

        cbProduto = new JComboBox<>();
        cbProduto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (ProdutoPerecivel p : EstoqueStore.get().getPerec()) cbProduto.addItem(p);
        cbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean focus) {
                super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof ProdutoPerecivel p)
                    setText(p.getNome() + " (val: " + p.getDataValidade() + ")");
                return this;
            }
        });

        addField(root, "Produto Perecível:", cbProduto);
        addField(root, "Quantidade:",        txtQuantidade = new JTextField());
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
        JButton btnSave = new JButton("Salvar");
        btnSave.setBackground(new Color(0, 120, 210));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> salvar());
        bar.add(btnCancel);
        bar.add(btnSave);
        return bar;
    }

    private void salvar() {
        try {
            ProdutoPerecivel prod = (ProdutoPerecivel) cbProduto.getSelectedItem();
            if (prod == null) return;
            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            if (qtd <= 0) { err("Quantidade deve ser maior que zero."); return; }
            int id = EstoqueStore.get().nextId();
            EstoqueStore.get().getLotes().add(new LoteEstoque(id, prod, qtd, LocalDate.now()));
            dispose();
        } catch (NumberFormatException ex) { err("Quantidade inválida."); }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
