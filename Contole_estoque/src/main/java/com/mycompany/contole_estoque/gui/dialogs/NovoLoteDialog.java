package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Diálogo para registrar uma nova entrada de estoque.
 * Lista todos os produtos cadastrados (perecíveis e não perecíveis).
 */
public class NovoLoteDialog extends JDialog {

    private static final Color BG_WHITE   = Color.WHITE;
    private static final Color BG_HEADER  = new Color(245, 247, 250);
    private static final Color BG_FIELD   = new Color(250, 251, 253);
    private static final Color BORDER_CLR = new Color(210, 213, 220);
    private static final Color ACCENT     = new Color(0, 120, 210);
    private static final Color TEXT_DIM   = new Color(100, 105, 120);
    private static final Color TEXT_MAIN  = new Color(30, 32, 40);

    private JComboBox<Produto> cbProduto;
    private JTextField         txtQuantidade;

    /** Lista unificada de todos os produtos disponíveis. */
    private final List<Produto> todosProdutos = new ArrayList<>();

    public NovoLoteDialog(JFrame owner) {
        super(owner, "Nova Entrada de Estoque", true);
        setSize(460, 300);
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_WHITE);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(),   BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 14));
        h.setBackground(BG_HEADER);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));

        JLabel icon  = new JLabel("📥");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel title = new JLabel("Registrar Entrada de Estoque");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(TEXT_MAIN);

        h.add(icon);
        h.add(title);
        return h;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(BG_WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(18, 26, 10, 26));

        // ── preenche lista unificada
        todosProdutos.addAll(EstoqueStore.get().getPerec());
        todosProdutos.addAll(EstoqueStore.get().getNaoPerec());

        cbProduto = new JComboBox<>();
        cbProduto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbProduto.setBackground(BG_FIELD);
        cbProduto.setForeground(TEXT_MAIN);
        cbProduto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cbProduto.setAlignmentX(LEFT_ALIGNMENT);

        for (Produto p : todosProdutos) cbProduto.addItem(p);

        cbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean focus) {
                super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof ProdutoPerecivel pp)
                    setText("🥩 " + pp.getNome() + "  (val: " + pp.getDataValidade() + ")");
                else if (v instanceof ProdutoNaoPerecivel np)
                    setText("🥫 " + np.getNome());
                return this;
            }
        });

        txtQuantidade = new JTextField();
        txtQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtQuantidade.setForeground(TEXT_MAIN);
        txtQuantidade.setBackground(BG_FIELD);
        txtQuantidade.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtQuantidade.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                txtQuantidade.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT, 1, true), new EmptyBorder(5, 10, 5, 10)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                txtQuantidade.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_CLR, 1, true), new EmptyBorder(5, 10, 5, 10)));
            }
        });

        addRow(body, "Produto", cbProduto);
        addRow(body, "Quantidade", txtQuantidade);
        return body;
    }

    private JPanel buildFooter() {
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        foot.setBackground(BG_HEADER);
        foot.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancel.setForeground(TEXT_DIM);
        btnCancel.setBackground(new Color(235, 237, 242));
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Registrar");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(ACCENT);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setBorder(new EmptyBorder(8, 22, 8, 22));
        btnSave.addActionListener(e -> salvar());

        foot.add(btnCancel);
        foot.add(btnSave);
        return foot;
    }

    private void addRow(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_DIM);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        panel.add(Box.createVerticalStrut(6));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
    }

    private void salvar() {
        try {
            Produto prod = (Produto) cbProduto.getSelectedItem();
            if (prod == null) { err("Selecione um produto."); return; }

            String qtdTxt = txtQuantidade.getText().trim();
            if (qtdTxt.isEmpty()) { err("Informe a quantidade."); return; }
            int qtd = Integer.parseInt(qtdTxt);
            if (qtd <= 0) { err("Quantidade deve ser maior que zero."); return; }

            int id = EstoqueStore.get().nextId();
            EstoqueStore.get().getLotes().add(
                new LoteEstoque(id, prod, qtd, LocalDate.now()));
            EstoqueStore.get().gerarAlertas();
            dispose();

        } catch (NumberFormatException ex) {
            err("Quantidade inválida. Digite um número inteiro.");
        }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
