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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta janela é um "Diálogo" que aparece para o usuário cadastrar uma nova entrada de estoque (Lote).
 * Agora com busca inteligente de produtos.
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

    private JComboBox<Produto> cbProduto;
    private JTextField         txtLote;
    private JTextField         txtQuantidade;
    private JTextField         txtValidade;
    private JPanel             pnlValidade;

    private final List<Produto> todosProdutos = new ArrayList<>();
    private boolean isFiltering = false;

    public NovoLoteDialog(JFrame owner) {
        super(owner, "Nova Entrada de Estoque", true);
        setSize(460, 420);
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

        JLabel icon  = new JLabel("\uD83D\uDCE5");
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

        todosProdutos.addAll(EstoqueStore.get().getPerec());
        todosProdutos.addAll(EstoqueStore.get().getNaoPerec());
        todosProdutos.sort((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()));

        cbProduto = new JComboBox<>();
        cbProduto.setEditable(true); // Permite digitar para buscar
        cbProduto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbProduto.setBackground(BG_FIELD);
        cbProduto.setForeground(TEXT_MAIN);
        cbProduto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cbProduto.setAlignmentX(LEFT_ALIGNMENT);

        // Configura o campo de texto do ComboBox
        JTextField editor = (JTextField) cbProduto.getEditor().getEditorComponent();
        editor.putClientProperty("JTextField.placeholderText", "Digite o nome do produto...");
        
        // Adiciona os produtos inicialmente
        refreshCombo(todosProdutos);
        cbProduto.setSelectedItem(null); // Inicia vazio para não mostrar o toString do primeiro item
        editor.setText("");

        // Lógica de busca em tempo real
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
            
            private void filtrar() {
                if (isFiltering) return;
                SwingUtilities.invokeLater(() -> {
                    isFiltering = true;
                    String busca = editor.getText().toLowerCase();
                    List<Produto> filtrados = todosProdutos.stream()
                            .filter(p -> p.getNome().toLowerCase().contains(busca))
                            .collect(Collectors.toList());
                    
                    refreshCombo(filtrados);
                    editor.setText(busca); // Mantém o texto digitado
                    if (!filtrados.isEmpty() && !busca.isEmpty()) {
                        cbProduto.setPopupVisible(true);
                    }
                    isFiltering = false;
                });
            }
        });

        cbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean focus) {
                super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof ProdutoPerecivel pp)
                    setText("\uD83E\uDD69 " + pp.getNome().toUpperCase() + "  (perecível)");
                else if (v instanceof ProdutoNaoPerecivel np)
                    setText("\uD83E\uDD6B " + np.getNome().toUpperCase());
                return this;
            }
        });

        cbProduto.addActionListener(e -> {
            if (!isFiltering) atualizarCampoValidade();
        });

        txtLote = field();
        txtQuantidade = field();
        txtValidade = field();
        txtValidade.setToolTipText("Ex: 31/12/2026");

        pnlValidade = new JPanel();
        pnlValidade.setBackground(BG_WHITE);
        pnlValidade.setLayout(new BoxLayout(pnlValidade, BoxLayout.Y_AXIS));
        pnlValidade.setAlignmentX(LEFT_ALIGNMENT);
        addRow(pnlValidade, "Data de Validade (dd/MM/aaaa)", txtValidade);

        addRow(body, "Pesquisar Produto", cbProduto);
        addRow(body, "Número do Lote",  txtLote);
        addRow(body, "Quantidade",      txtQuantidade);
        body.add(pnlValidade);

        atualizarCampoValidade();
        return body;
    }

    private void refreshCombo(List<Produto> lista) {
        cbProduto.removeAllItems();
        for (Produto p : lista) cbProduto.addItem(p);
    }

    private void atualizarCampoValidade() {
        Object item = cbProduto.getSelectedItem();
        boolean isPerec = item instanceof ProdutoPerecivel;
        pnlValidade.setVisible(isPerec);
        revalidate();
        repaint();
    }

    private JPanel buildFooter() {
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        foot.setBackground(BG_HEADER);
        foot.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Registrar");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(ACCENT);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
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
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_CLR, 1, true), new EmptyBorder(5, 10, 5, 10)));
        return tf;
    }

    private void salvar() {
        try {
            Object selected = cbProduto.getSelectedItem();
            if (!(selected instanceof Produto)) { 
                err("Selecione um produto válido da lista."); 
                return; 
            }
            Produto prod = (Produto) selected;

            String loteTxt = txtLote.getText().trim();
            if (loteTxt.isEmpty()) { err("Informe o número do lote."); return; }

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
                EstoqueStore.get().nextId(), Movimentacao.Tipo.INCLUSAO,
                novoLote, qtd, LocalDate.now(), "Entrada de estoque"
            ));

            EstoqueStore.get().gerarAlertas();
            dispose();

        } catch (NumberFormatException ex) {
            err("Quantidade inválida. Digite apenas números.");
        } catch (DateTimeParseException ex) {
            err("Data inválida. Use o formato dd/MM/aaaa.");
        }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}