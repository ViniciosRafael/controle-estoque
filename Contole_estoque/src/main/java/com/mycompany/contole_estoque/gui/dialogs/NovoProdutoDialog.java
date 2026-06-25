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
 *
 * Campos por tipo:
 *   Perecível     → Nome, Categoria, Preço Unitário, Data de Validade,
 *                   Estoque Mínimo, Quantidade do Lote inicial
 *   Não Perecível → Nome, Categoria, Preço Unitário, Estoque Mínimo
 */
public class NovoProdutoDialog extends JDialog {

    // ── paleta (dark-glass)
    private static final Color BG_DIALOG   = new Color(22, 24, 38);
    private static final Color BG_HEADER   = new Color(28, 30, 46);
    private static final Color BG_FIELD    = new Color(32, 35, 55);
    private static final Color ACCENT_PERC = new Color(16, 163, 127);
    private static final Color ACCENT_NAO  = new Color(0, 120, 210);
    private static final Color BORDER_CLR  = new Color(55, 58, 82);
    private static final Color TEXT_DIM    = new Color(140, 145, 175);
    private static final Color TEXT_MAIN   = new Color(220, 222, 235);

    // ── controles
    private JComboBox<String> cbTipo;

    private JTextField txtNome, txtCategoria, txtPreco, txtEstMin;
    private JTextField txtValidade, txtQtdLote;

    // painéis condicionais
    private JPanel pnlPerecFields;   // Validade + Lote  (apenas perecível)
    private JPanel pnlNaoFields;     // nada extra       (apenas não-perecível: já está em comum)

    private JButton btnSave;

    public NovoProdutoDialog(JFrame owner) {
        super(owner, "Novo Produto", true);
        setUndecorated(false);
        setSize(500, 540);
        setMinimumSize(new Dimension(460, 500));
        setLocationRelativeTo(owner);
        setResizable(false);
        buildUI();
        actualizarCampos();          // estado inicial
    }

    // ======================================================== construção UI ===

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DIALOG);
        setContentPane(root);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildBody(),    BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);
    }

    /** Cabeçalho colorido com título e ícone. */
    private JPanel buildHeader() {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        h.setBackground(BG_HEADER);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));

        JLabel icon = new JLabel("📦");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel title = new JLabel("Cadastrar Produto");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        h.add(icon);
        h.add(title);
        return h;
    }

    /** Corpo do formulário. */
    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(BG_DIALOG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(22, 28, 10, 28));

        // ── tipo (sempre visível)
        cbTipo = new JComboBox<>(new String[]{"Perecível", "Não Perecível"});
        styleCombo(cbTipo);
        cbTipo.addActionListener(e -> actualizarCampos());
        addRow(body, "Tipo de Produto", cbTipo);
        body.add(sep());

        // ── campos comuns
        txtNome      = styledField();
        txtCategoria = styledField();
        txtPreco     = styledField();
        txtEstMin    = styledField();
        addRow(body, "Nome", txtNome);
        addRow(body, "Categoria", txtCategoria);
        addRow(body, "Preço Unitário (R$)", txtPreco);
        addRow(body, "Estoque Mínimo", txtEstMin);

        // ── campos exclusivos de Perecível
        pnlPerecFields = new JPanel();
        pnlPerecFields.setBackground(BG_DIALOG);
        pnlPerecFields.setLayout(new BoxLayout(pnlPerecFields, BoxLayout.Y_AXIS));
        pnlPerecFields.setAlignmentX(LEFT_ALIGNMENT);

        txtValidade = styledField();
        txtQtdLote  = styledField();
        addRow(pnlPerecFields, "Data de Validade (aaaa-mm-dd)", txtValidade);
        addRow(pnlPerecFields, "Quantidade do Lote Inicial", txtQtdLote);

        body.add(pnlPerecFields);
        body.add(Box.createVerticalGlue());
        return body;
    }

    /** Rodapé com botões Cancelar / Salvar. */
    private JPanel buildFooter() {
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        foot.setBackground(BG_HEADER);
        foot.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancel.setForeground(TEXT_DIM);
        btnCancel.setBackground(BG_FIELD);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.setBorder(new EmptyBorder(9, 22, 9, 22));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCancel.setBackground(new Color(45, 48, 72));
                btnCancel.setForeground(Color.WHITE);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btnCancel.setBackground(BG_FIELD);
                btnCancel.setForeground(TEXT_DIM);
            }
        });
        btnCancel.addActionListener(e -> dispose());

        btnSave = new JButton("✔  Salvar");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(ACCENT_PERC);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setBorder(new EmptyBorder(9, 24, 9, 24));
        btnSave.addActionListener(e -> salvar());

        foot.add(btnCancel);
        foot.add(btnSave);
        return foot;
    }

    // ======================================================== helpers de UI ===

    private void addRow(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_DIM);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        panel.add(Box.createVerticalStrut(4));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        panel.add(Box.createVerticalStrut(6));
    }

    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TEXT_MAIN);
        tf.setBackground(BG_FIELD);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_PERC, 1, true),
                    new EmptyBorder(5, 10, 5, 10)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_CLR, 1, true),
                    new EmptyBorder(5, 10, 5, 10)));
            }
        });
        return tf;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setForeground(TEXT_MAIN);
        cb.setBackground(BG_FIELD);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cb.setAlignmentX(LEFT_ALIGNMENT);
    }

    private Component sep() {
        JPanel s = new JPanel();
        s.setBackground(BORDER_CLR);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        s.setAlignmentX(LEFT_ALIGNMENT);
        JPanel wrap = new JPanel();
        wrap.setBackground(BG_DIALOG);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(Box.createVerticalStrut(6));
        wrap.add(s);
        wrap.add(Box.createVerticalStrut(10));
        return wrap;
    }

    // ======================================================= lógica dinâmica ==

    /** Mostra/esconde campos conforme tipo selecionado. */
    private void actualizarCampos() {
        boolean isPerec = cbTipo.getSelectedIndex() == 0;

        pnlPerecFields.setVisible(isPerec);

        // Cor do botão Salvar acompanha o tipo
        btnSave.setBackground(isPerec ? ACCENT_PERC : ACCENT_NAO);

        // Redimensiona o diálogo para não sobrar espaço vazio
        setSize(500, isPerec ? 580 : 450);
        validate();
        repaint();
    }

    // ============================================================= persistir ==

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String cat  = txtCategoria.getText().trim();
            if (nome.isEmpty()) { err("O campo 'Nome' é obrigatório."); return; }
            if (cat.isEmpty())  { err("O campo 'Categoria' é obrigatório."); return; }

            String precoTxt = txtPreco.getText().trim();
            if (precoTxt.isEmpty()) { err("Informe o Preço Unitário."); return; }
            double preco = Double.parseDouble(precoTxt.replace(',', '.'));

            String estMinTxt = txtEstMin.getText().trim();
            if (estMinTxt.isEmpty()) { err("Informe o Estoque Mínimo."); return; }
            int estMin = Integer.parseInt(estMinTxt);
            if (estMin < 0) { err("Estoque Mínimo não pode ser negativo."); return; }

            int id = EstoqueStore.get().nextId();

            if (cbTipo.getSelectedIndex() == 0) {           // ── Perecível
                String valTxt = txtValidade.getText().trim();
                String qtdTxt = txtQtdLote.getText().trim();
                if (valTxt.isEmpty()) { err("Informe a Data de Validade."); return; }
                if (qtdTxt.isEmpty()) { err("Informe a Quantidade do Lote."); return; }

                LocalDate validade = LocalDate.parse(valTxt);
                int qtdLote = Integer.parseInt(qtdTxt);
                if (qtdLote <= 0) { err("Quantidade do Lote deve ser maior que zero."); return; }

                ProdutoPerecivel prod = new ProdutoPerecivel(id, nome, cat, validade, estMin, preco);
                EstoqueStore.get().getPerec().add(prod);

                int idLote = EstoqueStore.get().nextId();
                EstoqueStore.get().getLotes().add(
                    new com.mycompany.contole_estoque.LoteEstoque(
                        idLote, prod, qtdLote, LocalDate.now()));
                EstoqueStore.get().gerarAlertas();

            } else {                                         // ── Não Perecível
                EstoqueStore.get().getNaoPerec().add(
                    new ProdutoNaoPerecivel(id, nome, cat, preco, estMin));
            }

            dispose();

        } catch (NumberFormatException ex) {
            err("Valor numérico inválido. Verifique Preço, Estoque Mínimo e Quantidade.");
        } catch (DateTimeParseException ex) {
            err("Data inválida. Use o formato aaaa-mm-dd (ex: 2026-12-31).");
        }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }
}
