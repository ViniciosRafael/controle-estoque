package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.LoteEstoque;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Diálogo para dar baixa de quantidade em um lote de estoque.
 */
public class DarBaixaDialog extends JDialog {

    private final LoteEstoque lote;
    private JTextField        txtQuantidade;

    public DarBaixaDialog(JFrame owner, LoteEstoque lote) {
        super(owner, "Dar Baixa no Lote #" + lote.getIdLote(), true);
        this.lote = lote;
        setSize(400, 240);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 28, 22, 28));

        JLabel produto = new JLabel("Produto:  " + lote.getProduto().getNome());
        produto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produto.setAlignmentX(LEFT_ALIGNMENT);
        root.add(produto);
        root.add(Box.createVerticalStrut(6));

        JLabel dispLabel = new JLabel("Disponível: " + lote.getQuantidade() + " unidades");
        dispLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dispLabel.setForeground(new Color(130, 130, 155));
        dispLabel.setAlignmentX(LEFT_ALIGNMENT);
        root.add(dispLabel);
        root.add(Box.createVerticalStrut(18));

        JLabel lbl = new JLabel("Quantidade a baixar:");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        root.add(lbl);
        root.add(Box.createVerticalStrut(4));

        txtQuantidade = new JTextField();
        txtQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtQuantidade.setAlignmentX(LEFT_ALIGNMENT);
        txtQuantidade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        root.add(txtQuantidade);
        root.add(Box.createVerticalStrut(18));
        root.add(buttonBar());

        setContentPane(root);
    }

    private JPanel buttonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());
        JButton btnOk = new JButton("Confirmar");
        btnOk.setBackground(new Color(200, 125, 0));
        btnOk.setForeground(Color.WHITE);
        btnOk.setBorderPainted(false);
        btnOk.addActionListener(e -> confirmar());
        bar.add(btnCancel);
        bar.add(btnOk);
        return bar;
    }

    private void confirmar() {
        try {
            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            if (qtd <= 0 || qtd > lote.getQuantidade()) {
                JOptionPane.showMessageDialog(this,
                    "Quantidade inválida (máx: " + lote.getQuantidade() + ").",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            lote.darBaixa(qtd);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
