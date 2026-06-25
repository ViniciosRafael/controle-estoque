package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Diálogo para cadastrar um novo Produto (perecível ou não perecível).
 * Campos condicionais aparecem ao escolher o tipo.
 */
public class NovoProdutoDialog extends JDialog {

    private JComboBox<String> cbTipo;
    private JTextField txtNome, txtCategoria, txtPreco;
    private JTextField txtValidade, txtEstMin;
    private JPanel     perecArea;

    public NovoProdutoDialog(JFrame owner) {
        super(owner, "Novo Produto", true);
        setSize(460, 440);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 28, 22, 28));

        cbTipo = new JComboBox<>(new String[]{"Perecível", "Não Perecível"});
        addField(root, "Tipo:", cbTipo);
        addField(root, "Nome:",             txtNome      = new JTextField());
        addField(root, "Categoria:",        txtCategoria = new JTextField());
        addField(root, "Preço Unitário (R$):", txtPreco  = new JTextField());

        // perecível-only fields
        perecArea = new JPanel();
        perecArea.setLayout(new BoxLayout(perecArea, BoxLayout.Y_AXIS));
        perecArea.setOpaque(false);
        addField(perecArea, "Data de Validade (aaaa-mm-dd):", txtValidade = new JTextField());
        addField(perecArea, "Estoque Mínimo:",                txtEstMin   = new JTextField());
        root.add(perecArea);

        cbTipo.addActionListener(e -> perecArea.setVisible(cbTipo.getSelectedIndex() == 0));

        root.add(Box.createVerticalStrut(14));
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
        btnSave.setBackground(new Color(16, 163, 127));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> salvar());

        bar.add(btnCancel);
        bar.add(btnSave);
        return bar;
    }

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String cat  = txtCategoria.getText().trim();
            if (nome.isEmpty() || cat.isEmpty()) { err("Preencha nome e categoria."); return; }
            double preco = Double.parseDouble(txtPreco.getText().trim().replace(',', '.'));
            int id = EstoqueStore.get().nextId();

            if (cbTipo.getSelectedIndex() == 0) {
                LocalDate val = LocalDate.parse(txtValidade.getText().trim());
                int estMin    = Integer.parseInt(txtEstMin.getText().trim());
                EstoqueStore.get().getPerec().add(
                    new ProdutoPerecivel(id, nome, cat, val, estMin, preco));
            } else {
                EstoqueStore.get().getNaoPerec().add(
                    new ProdutoNaoPerecivel(id, nome, cat, preco));
            }
            dispose();
        } catch (NumberFormatException ex)    { err("Valor ou estoque mínimo inválido."); }
          catch (DateTimeParseException ex)   { err("Data inválida. Use o formato aaaa-mm-dd."); }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
