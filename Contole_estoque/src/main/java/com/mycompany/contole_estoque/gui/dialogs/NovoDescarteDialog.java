package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Diálogo para registrar um novo Descarte de lote.
 *
 * Correções aplicadas:
 * - Exibe o numeroLote (código do lote) em vez do idLote interno.
 * - Permite descartar lotes com quantidade zero (ex.: lotes vencidos já zerados).
 * - Registra a movimentação no histórico unificado.
 * - Suporta lote pré-selecionado quando aberto a partir da aba Estoque.
 */
public class NovoDescarteDialog extends JDialog {

    private JComboBox<LoteEstoque> cbLote;
    private JTextField             txtQuantidade, txtMotivo;

    /** Abre o diálogo sem pré-seleção de lote (usado na aba Histórico). */
    public NovoDescarteDialog(JFrame owner) {
        this(owner, null);
    }

    /** Abre o diálogo com um lote pré-selecionado (usado na aba Estoque). */
    public NovoDescarteDialog(JFrame owner, LoteEstoque lotePre) {
        super(owner, "Registrar Descarte", true);
        setSize(500, 320);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI(lotePre);
    }

    private void buildUI(LoteEstoque lotePre) {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 28, 22, 28));

        cbLote = new JComboBox<>();
        cbLote.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        int preIndex = -1;
        int idx = 0;
        for (LoteEstoque l : EstoqueStore.get().getLotes()) {
            // Inclui lotes com produto válido, mesmo que quantidade seja zero
            // (ex.: lotes vencidos que já tiveram baixa total mas ainda precisam
            // ser formalmente descartados/registrados no histórico).
            if (l.getProduto() != null) {
                cbLote.addItem(l);
                if (lotePre != null && l.getIdLote() == lotePre.getIdLote()) {
                    preIndex = idx;
                }
                idx++;
            }
        }

        // Pré-seleciona o lote se foi passado como parâmetro
        if (preIndex >= 0) {
            cbLote.setSelectedIndex(preIndex);
        }

        cbLote.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean focus) {
                super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof LoteEstoque lt) {
                    // Usa o numeroLote (código visível ao usuário), não o idLote interno
                    String numLote = lt.getNumeroLote() != null ? lt.getNumeroLote() : String.valueOf(lt.getIdLote());
                    setText("Lote: " + numLote + "  —  " +
                            lt.getProduto().getNome() + "  (qtd: " + lt.getQuantidade() + ")");
                }
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
            if (lote == null) { err("Nenhum lote disponível para descarte."); return; }

            String qtdTxt = txtQuantidade.getText().trim();
            if (qtdTxt.isEmpty()) { err("Informe a quantidade a descartar."); return; }
            int qtd = Integer.parseInt(qtdTxt);

            String motivo = txtMotivo.getText().trim();
            if (motivo.isEmpty()) { err("Informe o motivo do descarte."); return; }

            // Permite qtd=0 apenas para lotes já zerados (registro formal de descarte)
            if (qtd < 0) { err("Quantidade não pode ser negativa."); return; }
            if (qtd > lote.getQuantidade()) {
                err("Quantidade inválida. Disponível no lote: " + lote.getQuantidade() + " unidades."); return;
            }

            int id = EstoqueStore.get().nextId();
            Descarte d = new Descarte(id, lote, qtd, LocalDate.now(), motivo);
            // Só aplica darBaixa se ainda há quantidade (evita erro em lotes zerados)
            if (qtd > 0) {
                lote.darBaixa(qtd);
            }
            EstoqueStore.get().getDescartes().add(d);

            // Registra no histórico unificado de movimentações
            int movId = EstoqueStore.get().nextId();
            Movimentacao mov = new Movimentacao(movId, TipoMovimentacao.DESCARTE,
                    lote, qtd, LocalDate.now(), motivo);
            EstoqueStore.get().getMovimentacoes().add(mov);

            dispose();
        } catch (NumberFormatException ex) { err("Quantidade inválida. Digite um número inteiro."); }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
