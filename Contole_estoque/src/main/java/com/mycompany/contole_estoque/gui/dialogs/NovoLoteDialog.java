package com.mycompany.contole_estoque.gui.dialogs;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.Movimentacao;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialogo para registrar uma nova entrada de estoque (Lote).
 * Busca de produtos com autocomplete limpo via JTextField + JPopupMenu.
 */
public class NovoLoteDialog extends JDialog {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final Color BG_WHITE   = Tema.FUNDO;
    private static final Color BG_HEADER  = Tema.HEADER_BG;
    private static final Color BG_FIELD   = Tema.CAMPO_BG;
    private static final Color BORDER_CLR = Tema.BORDA;
    private static final Color ACCENT     = Tema.PRIMARIA;
    private static final Color TEXT_DIM   = Tema.TEXTO_SUB;
    private static final Color TEXT_MAIN  = Tema.TEXTO_TITULO;

    // Campo de busca customizado
    private JTextField txtBusca;
    private JPopupMenu popup;
    private JList<Produto> listaSugestoes;
    private DefaultListModel<Produto> modeloLista;

    private JTextField txtLote;
    private JTextField txtQuantidade;
    private JTextField txtValidade;
    private JPanel     pnlValidade;

    private Produto produtoSelecionado = null;
    private final List<Produto> todosProdutos = new ArrayList<>();

    public NovoLoteDialog(JFrame owner) {
        super(owner, "Nova Entrada de Estoque", true);
        setSize(480, 440);
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

        JLabel title = new JLabel("Registrar Entrada de Estoque");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(TEXT_MAIN);

        h.add(title);
        return h;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(BG_WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(18, 26, 10, 26));

        todosProdutos.addAll(EstoqueStore.get().getPerec());
        todosProdutos.addAll(EstoqueStore.get().getNaoPerec());
        todosProdutos.sort((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()));

        // Campo de busca
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBusca.setForeground(TEXT_MAIN);
        txtBusca.setBackground(BG_FIELD);
        txtBusca.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        txtBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtBusca.setAlignmentX(LEFT_ALIGNMENT);
        txtBusca.putClientProperty("JTextField.placeholderText", "Digite o nome do produto...");

        // Popup de sugestoes
        modeloLista = new DefaultListModel<>();
        listaSugestoes = new JList<>(modeloLista);
        listaSugestoes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        listaSugestoes.setBackground(BG_WHITE);
        listaSugestoes.setForeground(TEXT_MAIN);
        listaSugestoes.setSelectionBackground(ACCENT);
        listaSugestoes.setSelectionForeground(Color.WHITE);
        listaSugestoes.setFixedCellHeight(30);
        listaSugestoes.setCellRenderer(new ProdutoRenderer());

        JScrollPane scroll = new JScrollPane(listaSugestoes);
        scroll.setBorder(new LineBorder(BORDER_CLR, 1));

        popup = new JPopupMenu();
        popup.setLayout(new BorderLayout());
        popup.add(scroll, BorderLayout.CENTER);
        popup.setFocusable(false);

        // Eventos do campo de busca
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { SwingUtilities.invokeLater(() -> filtrar()); }
            @Override public void removeUpdate(DocumentEvent e)  { SwingUtilities.invokeLater(() -> filtrar()); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        txtBusca.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (!popup.isVisible()) return;
                int idx = listaSugestoes.getSelectedIndex();
                int sz  = modeloLista.getSize();
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    listaSugestoes.setSelectedIndex(Math.min(idx + 1, sz - 1));
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    listaSugestoes.setSelectedIndex(Math.max(idx - 1, 0));
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selecionarProduto(listaSugestoes.getSelectedValue());
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    popup.setVisible(false);
                    e.consume();
                }
            }
        });

        listaSugestoes.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selecionarProduto(listaSugestoes.getSelectedValue());
            }
        });

        txtLote       = field();
        txtQuantidade = field();
        txtValidade   = field();
        txtValidade.setToolTipText("Ex: 31/12/2026");

        pnlValidade = new JPanel();
        pnlValidade.setBackground(BG_WHITE);
        pnlValidade.setLayout(new BoxLayout(pnlValidade, BoxLayout.Y_AXIS));
        pnlValidade.setAlignmentX(LEFT_ALIGNMENT);
        addRow(pnlValidade, "Data de Validade (dd/MM/aaaa)", txtValidade);

        addRow(body, "Pesquisar Produto", txtBusca);
        addRow(body, "Numero do Lote",    txtLote);
        addRow(body, "Quantidade",         txtQuantidade);
        body.add(pnlValidade);

        atualizarCampoValidade();
        return body;
    }

    private void filtrar() {
        String termo = txtBusca.getText().trim().toLowerCase();

        if (produtoSelecionado != null && txtBusca.getText().trim()
                .equalsIgnoreCase(produtoSelecionado.getNome())) {
            return;
        }

        produtoSelecionado = null;
        atualizarCampoValidade();

        List<Produto> filtrados = todosProdutos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(termo))
                .collect(Collectors.toList());

        modeloLista.clear();
        for (Produto p : filtrados) modeloLista.addElement(p);

        if (!filtrados.isEmpty() && !termo.isEmpty()) {
            int largura = txtBusca.getWidth();
            int altura  = Math.min(filtrados.size() * 30, 180);
            popup.setPreferredSize(new Dimension(largura, altura));
            if (!popup.isVisible()) {
                popup.show(txtBusca, 0, txtBusca.getHeight());
                txtBusca.requestFocusInWindow();
            }
        } else {
            popup.setVisible(false);
        }
    }

    private void selecionarProduto(Produto p) {
        if (p == null) return;
        produtoSelecionado = p;
        txtBusca.setText(p.getNome());
        txtBusca.setCaretPosition(txtBusca.getText().length());
        popup.setVisible(false);
        atualizarCampoValidade();
    }

    private void atualizarCampoValidade() {
        boolean isPerec = produtoSelecionado instanceof ProdutoPerecivel;
        pnlValidade.setVisible(isPerec);
        revalidate();
        repaint();
    }

    private JPanel buildFooter() {
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        foot.setBackground(BG_HEADER);
        foot.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Registrar");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(ACCENT);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        btnSave.setPreferredSize(new Dimension(100, 34));
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
        panel.add(Box.createVerticalStrut(8));
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
            new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        return tf;
    }

    private void salvar() {
        try {
            if (produtoSelecionado == null) {
                err("Selecione um produto valido da lista de sugestoes.");
                return;
            }
            Produto prod = produtoSelecionado;

            String loteTxt = txtLote.getText().trim();
            if (loteTxt.isEmpty()) { err("Informe o numero do lote."); return; }

            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            if (qtd <= 0) { err("A quantidade deve ser maior que zero."); return; }

            LocalDate validade = null;
            if (prod instanceof ProdutoPerecivel) {
                String valTxt = txtValidade.getText().trim();
                if (valTxt.isEmpty()) { err("Informe a Data de Validade."); return; }
                validade = LocalDate.parse(valTxt, DTF);
            }

            int id = EstoqueStore.get().nextId();
            LoteEstoque novoLote = new LoteEstoque(id, loteTxt, prod, qtd, LocalDate.now(), validade);
            EstoqueStore.get().getLotes().add(novoLote);

            EstoqueStore.get().getMovimentacoes().add(new Movimentacao(
                EstoqueStore.get().nextId(), TipoMovimentacao.INCLUSAO,
                novoLote, qtd, LocalDate.now(), "Entrada de estoque"
            ));

            EstoqueStore.get().gerarAlertas();
            dispose();

        } catch (NumberFormatException ex) {
            err("Quantidade invalida. Digite apenas numeros inteiros.");
        } catch (DateTimeParseException ex) {
            err("Data invalida. Use o formato dd/MM/aaaa.");
        }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static class ProdutoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ProdutoPerecivel pp) {
                setText(pp.getNome() + "  (perecivel)");
            } else if (value instanceof ProdutoNaoPerecivel np) {
                setText(np.getNome());
            } else if (value instanceof Produto p) {
                setText(p.getNome());
            }
            setBorder(new EmptyBorder(0, 6, 0, 6));
            return this;
        }
    }
}
