package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.LoteEstoque;
import com.mycompany.contole_estoque.Movimentacao;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Diálogo para dar baixa de quantidade em um lote de estoque.
 * Registra a movimentação no histórico unificado.
 */
public class DarBaixaDialog extends JDialog {

    private final LoteEstoque lote;
    private JTextField        txtQuantidade;
    private JTextField        txtObservacao;

    public DarBaixaDialog(JFrame owner, LoteEstoque lote) {
        super(owner, "Dar Baixa — Lote: " + (lote.getNumeroLote() != null ? lote.getNumeroLote() : lote.getIdLote()), true);
        this.lote = lote;
        setSize(420, 290);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 28, 22, 28));

        String nomeProduto = (lote.getProduto() != null) ? lote.getProduto().getNome() : "(produto removido)";
        JLabel produto = new JLabel("Produto:  " + nomeProduto);
        produto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produto.setAlignmentX(LEFT_ALIGNMENT);
        root.add(produto);
        root.add(Box.createVerticalStrut(4));

        String numLote = lote.getNumeroLote() != null ? lote.getNumeroLote() : String.valueOf(lote.getIdLote());
        JLabel loteLabel = new JLabel("Lote: " + numLote);
        loteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loteLabel.setForeground(new Color(130, 130, 155));
        loteLabel.setAlignmentX(LEFT_ALIGNMENT);
        root.add(loteLabel);
        root.add(Box.createVerticalStrut(4));

        JLabel dispLabel = new JLabel("Disponível: " + lote.getQuantidade() + " unidades");
        dispLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dispLabel.setForeground(new Color(130, 130, 155));
        dispLabel.setAlignmentX(LEFT_ALIGNMENT);
        root.add(dispLabel);
        root.add(Box.createVerticalStrut(14));

        addField(root, "Quantidade a baixar:", txtQuantidade = new JTextField());
        addField(root, "Observação (opcional):", txtObservacao = new JTextField());
        root.add(Box.createVerticalStrut(8));
        root.add(buttonBar());

        setContentPane(root);
    }

    private void addField(JPanel panel, String label, JTextField field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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

            // Registra no histórico unificado de movimentações
            String obs = txtObservacao.getText().trim();
            if (obs.isEmpty()) obs = "Baixa manual";
            int movId = EstoqueStore.get().nextId();
            Movimentacao mov = new Movimentacao(movId, Movimentacao.Tipo.BAIXA,
                    lote, qtd, LocalDate.now(), obs);
            EstoqueStore.get().getMovimentacoes().add(mov);

            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
