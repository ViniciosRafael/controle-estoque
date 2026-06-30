package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.config.ConfiguracoesStore;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diálogo para cadastrar um novo Produto (perecível ou não perecível).
 *
 * Perecível     → Nome, Categoria, Preço Unitário, Estoque Mínimo
 * Não Perecível → Nome, Categoria, Preço Unitário, Estoque Mínimo
 *
 * A data de validade é registrada por lote, não por produto.
 */
public class NovoProdutoDialog extends JDialog {

    // ── paleta (delega para Tema, tema escuro)
    private static final Color BG_WHITE    = Tema.FUNDO;
    private static final Color BG_HEADER   = Tema.HEADER_BG;
    private static final Color BG_FIELD    = Tema.CAMPO_BG;
    private static final Color BORDER_CLR  = Tema.BORDA;
    private static final Color ACCENT_BLUE = Tema.PRIMARIA;
    private static final Color TEXT_DIM    = Tema.TEXTO_SUB;
    private static final Color TEXT_MAIN   = Tema.TEXTO_TITULO;

    // ── controles
    private JComboBox<String> cbTipo;
    private JTextField txtNome, txtCategoria, txtPreco, txtEstMin;
    private JButton btnSave;

    public NovoProdutoDialog(JFrame owner) {
        super(owner, "Novo Produto", true);
        setSize(480, 460);
        setMinimumSize(new Dimension(460, 420));
        setLocationRelativeTo(owner);
        setResizable(true);
        buildUI();
    }

    // =========================================================== construção UI
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_WHITE);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildBody());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        root.add(scroll, BorderLayout.CENTER);

        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 14));
        h.setBackground(BG_HEADER);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));

        JLabel title = new JLabel("Cadastrar Produto");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(TEXT_MAIN);

        h.add(title);
        return h;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(BG_WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(18, 26, 10, 26));

        // Tipo — só lista os tipos atualmente habilitados em Configurações
        cbTipo = new JComboBox<>(tiposDisponiveis());
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbTipo.setBackground(BG_FIELD);
        cbTipo.setForeground(TEXT_MAIN);
        cbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cbTipo.setAlignmentX(LEFT_ALIGNMENT);

        addRow(body, "Tipo de Produto", cbTipo);
        body.add(divider());

        // Campos comuns
        txtNome      = field();
        txtCategoria = field();
        txtPreco     = field();
        txtEstMin    = field();

        addRow(body, "Nome",                 txtNome);
        addRow(body, "Categoria",            txtCategoria);
        addRow(body, "Preço Unitário (R$)",  txtPreco);
        addRow(body, "Estoque Mínimo",       txtEstMin);

        return body;
    }

    private JPanel buildFooter() {
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        foot.setBackground(BG_HEADER);
        foot.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancel.setForeground(TEXT_DIM);
        btnCancel.setBackground(Tema.CAMPO_BG);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnCancel.addActionListener(e -> dispose());

        btnSave = new JButton("Salvar");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(ACCENT_BLUE);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setBorder(new EmptyBorder(8, 22, 8, 22));
        btnSave.addActionListener(e -> salvar());

        foot.add(btnCancel);
        foot.add(btnSave);
        return foot;
    }

    // =========================================================== helpers UI
    /** Monta a lista de tipos de produto liberados em Configurações para novo cadastro. */
    private String[] tiposDisponiveis() {
        List<String> tipos = new ArrayList<>();
        if (ConfiguracoesStore.get().isPereciveisHabilitados())    tipos.add("Perecível");
        if (ConfiguracoesStore.get().isNaoPereciveisHabilitados()) tipos.add("Não Perecível");
        return tipos.toArray(String[]::new);
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

    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TEXT_MAIN);
        tf.setBackground(BG_FIELD);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_BLUE, 1, true),
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

    private Component divider() {
        JPanel d = new JPanel();
        d.setBackground(BORDER_CLR);
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setAlignmentX(LEFT_ALIGNMENT);

        JPanel wrap = new JPanel();
        wrap.setBackground(BG_WHITE);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(Box.createVerticalStrut(8));
        wrap.add(d);
        wrap.add(Box.createVerticalStrut(8));
        return wrap;
    }

    // =========================================================== lógica
    private void salvar() {
        try {
            if (cbTipo.getItemCount() == 0) {
                err("Nenhum tipo de produto está habilitado em Configurações.");
                return;
            }

            String nome = txtNome.getText().trim().toUpperCase();
            String cat  = txtCategoria.getText().trim();
            if (nome.isEmpty()) { err("O campo 'Nome' é obrigatório."); return; }
            if (cat.isEmpty())  { err("O campo 'Categoria' é obrigatório."); return; }

            String precoTxt = txtPreco.getText().trim().replace(',', '.');
            if (precoTxt.isEmpty()) { err("Informe o Preço Unitário."); return; }
            double preco = Double.parseDouble(precoTxt);
            if (preco < 0) { err("Preço Unitário não pode ser negativo."); return; }

            String estMinTxt = txtEstMin.getText().trim();
            if (estMinTxt.isEmpty()) { err("Informe o Estoque Mínimo."); return; }
            int estMin = Integer.parseInt(estMinTxt);
            if (estMin < 0) { err("Estoque Mínimo não pode ser negativo."); return; }

            int id = EstoqueStore.get().nextId();
            String tipoSelecionado = (String) cbTipo.getSelectedItem();

            if ("Perecível".equals(tipoSelecionado)) {
                EstoqueStore.get().getPerec().add(
                    new ProdutoPerecivel(id, nome, cat, estMin, preco));
            } else {                                        // Não Perecível
                EstoqueStore.get().getNaoPerec().add(
                    new ProdutoNaoPerecivel(id, nome, cat, preco, estMin));
            }

            dispose();

        } catch (NumberFormatException ex) {
            err("Valor numérico inválido. Verifique Preço Unitário e Estoque Mínimo.");
        }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }
}