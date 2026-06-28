package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.config.ConfiguracoesStore;
import com.mycompany.contole_estoque.export.ExportadorExcel;
import com.mycompany.contole_estoque.gui.dialogs.NovoProdutoDialog;
import com.mycompany.contole_estoque.gui.theme.Tema;
import com.mycompany.contole_estoque.util.OrdenadorProdutosPorNome;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel de Produtos — exibe perecíveis e não-perecíveis em abas separadas.
 * Permite criar e remover produtos.
 */
public class ProdutosPanel extends JPanel {

    private static final DateTimeFormatter FMT     = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color             BTN_BLUE = Tema.PRIMARIA;
    private static final Color             BTN_RED  = Tema.CRITICO;

    private DefaultTableModel perecModel, naoModel, alfabeticaModel;
    private JTable            perecTable, naoTable, alfabeticaTable;
    private JLabel            lblTempoOrdenacao;

    private JTextField txtBuscaPerec, txtBuscaNao, txtBuscaAlfabetica;

    private JTabbedPane tabs;
    private JPanel      perecPanel, naoPerecPanel, alfabeticaPanel;
    private static final String TITULO_PEREC     = "  Perecíveis  ";
    private static final String TITULO_NAO_PEREC = "  Não Perecíveis  ";
    private static final String TITULO_ALFABETICA = "  Ordem Alfabética  ";

    public ProdutosPanel() {
        setBackground(Tema.FUNDO);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Produtos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Tema.TEXTO_TITULO);
        p.add(title, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);

        JButton btnExportar = actionButton("Exportar p/ Excel", Tema.ACENTO);
        btnExportar.addActionListener(e -> exportarParaExcel());
        btns.add(btnExportar);

        JButton btnNovo = actionButton("+ Novo Produto", BTN_BLUE);
        btnNovo.addActionListener(e -> {
            new NovoProdutoDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            refresh();
        });
        btns.add(btnNovo);

        p.add(btns, BorderLayout.EAST);
        return p;
    }

    /**
     * Abre um seletor de arquivos para o usuário escolher onde salvar o
     * Excel, gera o arquivo com {@link ExportadorExcel} (Perecíveis, Não
     * Perecíveis, Estoque/Lotes e Descartes, cada um em sua própria aba)
     * e avisa o resultado.
     */
    private void exportarParaExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar planilha Excel");
        chooser.setSelectedFile(new java.io.File("estoque.xlsx"));
        chooser.setFileFilter(new FileNameExtensionFilter("Planilha Excel (*.xlsx)", "xlsx"));

        int resultado = chooser.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) return;

        java.io.File arquivo = chooser.getSelectedFile();
        String caminho = arquivo.getAbsolutePath();
        if (!caminho.toLowerCase().endsWith(".xlsx")) {
            caminho += ".xlsx";
        }

        try {
            ExportadorExcel.exportar(caminho);
            JOptionPane.showMessageDialog(this,
                    "Dados exportados com sucesso para:\n" + caminho,
                    "Exportação concluída", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao exportar para Excel:\n" + ex.getMessage(),
                    "Erro na exportação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTabbedPane buildTabs() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        // FlatLaf: sublinha a aba selecionada com a cor de destaque
        tabs.putClientProperty("JTabbedPane.tabAreaAlignment", "leading");
        tabs.putClientProperty("JTabbedPane.minimumTabWidth", 120);

        // ── Perecíveis
        String[] pCols = { "ID", "Nome", "Categoria", "Preço Unit.", "Est. Mínimo" };
        perecModel = emptyModel(pCols);
        perecTable = buildTable(perecModel);
        perecTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        perecTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        perecTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        perecTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        txtBuscaPerec = buildCampoBusca();
        txtBuscaPerec.getDocument().addDocumentListener(simpleListener(this::refreshPerec));
        perecPanel = tablePanel(perecTable, perecModel, true, txtBuscaPerec);

        // ── Não Perecíveis
        String[] nCols = { "ID", "Nome", "Categoria", "Preço Unitário", "Est. Mínimo" };
        naoModel = emptyModel(nCols);
        naoTable = buildTable(naoModel);
        naoTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        naoTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        naoTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        naoTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        txtBuscaNao = buildCampoBusca();
        txtBuscaNao.getDocument().addDocumentListener(simpleListener(this::refreshNaoPerec));
        naoPerecPanel = tablePanel(naoTable, naoModel, false, txtBuscaNao);

        // ── Ordem Alfabética (todos os produtos, perecíveis + não perecíveis)
        String[] aCols = { "ID", "Nome", "Categoria", "Tipo" };
        alfabeticaModel = emptyModel(aCols);
        alfabeticaTable = buildTable(alfabeticaModel);
        alfabeticaTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        alfabeticaTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        alfabeticaTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        alfabeticaPanel = new JPanel(new BorderLayout(0, 8));
        alfabeticaPanel.setOpaque(false);
        alfabeticaPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel alfabeticaBar = new JPanel(new BorderLayout(8, 0));
        alfabeticaBar.setOpaque(false);
        txtBuscaAlfabetica = buildCampoBusca();
        txtBuscaAlfabetica.getDocument().addDocumentListener(simpleListener(this::refreshOrdemAlfabetica));
        alfabeticaBar.add(txtBuscaAlfabetica, BorderLayout.WEST);
        lblTempoOrdenacao = new JLabel("Tempo de ordenação: -");
        lblTempoOrdenacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTempoOrdenacao.setForeground(Tema.TEXTO_SUB);
        alfabeticaBar.add(lblTempoOrdenacao, BorderLayout.EAST);
        alfabeticaPanel.add(alfabeticaBar, BorderLayout.NORTH);

        JScrollPane alfabeticaScroll = new JScrollPane(alfabeticaTable);
        alfabeticaScroll.setBorder(BorderFactory.createEmptyBorder());
        alfabeticaPanel.add(alfabeticaScroll, BorderLayout.CENTER);

        atualizarVisibilidadeAbas();

        return tabs;
    }

    /**
     * Adiciona ou remove as abas "Perecíveis" e "Não Perecíveis" conforme a
     * configuração atual em {@link ConfiguracoesStore}. Quando um tipo é
     * desabilitado em Configurações, a aba correspondente desaparece daqui;
     * quando reabilitado, a aba volta a aparecer.
     *
     * A aba "Ordem Alfabética" permanece sempre visível, pois ela mostra os
     * produtos cadastrados independentemente do tipo.
     */
    private void atualizarVisibilidadeAbas() {
        boolean pereciveisHabilitados    = ConfiguracoesStore.get().isPereciveisHabilitados();
        boolean naoPereciveisHabilitados = ConfiguracoesStore.get().isNaoPereciveisHabilitados();

        // Remove todas as abas e reconstrói na ordem correta — mais simples e
        // confiável do que tentar inserir/remover em posições específicas.
        tabs.removeAll();

        if (pereciveisHabilitados) {
            tabs.addTab(TITULO_PEREC, perecPanel);
        }
        if (naoPereciveisHabilitados) {
            tabs.addTab(TITULO_NAO_PEREC, naoPerecPanel);
        }
        tabs.addTab(TITULO_ALFABETICA, alfabeticaPanel);
    }

    private JPanel tablePanel(JTable table, DefaultTableModel model, boolean isPerec, JTextField campoBusca) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setOpaque(false);
        bar.add(campoBusca, BorderLayout.WEST);

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setOpaque(false);
        JButton btnRem = actionButton("Remover", BTN_RED);
        btnRem.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow < 0) { warn("Selecione um produto."); return; }
            int modelRow = table.convertRowIndexToModel(viewRow);
            if (JOptionPane.showConfirmDialog(this, "Remover produto selecionado?",
                    "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int id = (int) model.getValueAt(modelRow, 0);
                if (isPerec) EstoqueStore.get().getPerec().removeIf(pp -> pp.getId() == id);
                else         EstoqueStore.get().getNaoPerec().removeIf(pp -> pp.getId() == id);
                refresh();
            }
        });
        direita.add(btnRem);
        bar.add(direita, BorderLayout.EAST);
        p.add(bar, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    // ----------------------------------------------------------------- refresh
    public void refresh() {
        atualizarVisibilidadeAbas();
        refreshPerec();
        refreshNaoPerec();
        refreshOrdemAlfabetica();
    }

    private void refreshPerec() {
        perecModel.setRowCount(0);
        for (ProdutoPerecivel p : EstoqueStore.get().getPerec()) {
            perecModel.addRow(new Object[] {
                    p.getId(), p.getNome(), p.getCategoria(),
                    String.format("R$ %.2f", p.getPrecoUnitario()),
                    p.getEstoqueMinimo()
            });
        }
        aplicarFiltro(perecTable, txtBuscaPerec, 1);
    }

    private void refreshNaoPerec() {
        naoModel.setRowCount(0);
        for (ProdutoNaoPerecivel p : EstoqueStore.get().getNaoPerec()) {
            naoModel.addRow(new Object[]{
                p.getId(), p.getNome(), p.getCategoria(),
                String.format("R$ %.2f", p.getPrecoUnitario()),
                p.getEstoqueMinimo()
            });
        }
        aplicarFiltro(naoTable, txtBuscaNao, 1);
    }

    /**
     * Junta perecíveis e não perecíveis numa única lista, ordena por nome
     * (mantendo o ID de cadastro de cada produto), mede o tempo gasto na
     * ordenação e popula a tabela da aba "Ordem Alfabética".
     */
    private void refreshOrdemAlfabetica() {
        List<Produto> todos = new ArrayList<>();
        todos.addAll(EstoqueStore.get().getPerec());
        todos.addAll(EstoqueStore.get().getNaoPerec());

        OrdenadorProdutosPorNome.ResultadoOrdenacaoPorNome<Produto> resultado =
                OrdenadorProdutosPorNome.ordenarPorNomeComTempo(todos);

        alfabeticaModel.setRowCount(0);
        for (Produto p : resultado.getProdutosOrdenados()) {
            String tipo = (p instanceof ProdutoPerecivel) ? "Perecível" : "Não Perecível";
            alfabeticaModel.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getCategoria(), tipo
            });
        }
        aplicarFiltro(alfabeticaTable, txtBuscaAlfabetica, 1);

        lblTempoOrdenacao.setText(String.format(
                "Tempo de ordenação: %.3f ms (%d produtos)",
                resultado.getTempoMilissegundos(), todos.size()));
    }

    /**
     * Filtra as linhas da tabela mantendo apenas as que COMEÇAM com o texto
     * digitado no campo de busca — considerando tanto o ID (coluna 0) quanto
     * o nome do produto (coluna {@code colunaNome}). A busca não diferencia
     * maiúsculas de minúsculas.
     *
     * Exemplos: digitar "alf" encontra "Alface" (pelo nome); digitar "52"
     * encontra o produto com ID 52, ou ID 520, 521... (pelo ID); digitar "q"
     * encontra "Queijo" mas NÃO "Requeijão" (não começa com "q").
     *
     * Usa {@link RowFilter}, que filtra apenas a exibição (a tabela
     * continua mostrando os dados reais do model por trás).
     */
    private void aplicarFiltro(JTable table, JTextField campoBusca, int colunaNome) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Tabelas grandes (ex.: 50.000 linhas) precisam de um TableRowSorter
        // já associado ao model; criamos um apenas uma vez por tabela.
        TableRowSorter<DefaultTableModel> sorter = getOrCreateSorter(table, model);

        String texto = campoBusca.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        String textoLower = texto.toLowerCase();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                // Coluna 0 = ID (número) — compara como texto, ex: "52" casa com ID 52, 520, 521...
                String id = String.valueOf(entry.getValue(0));
                if (id.startsWith(textoLower)) return true;

                // Coluna colunaNome = Nome do produto
                Object valorNome = entry.getValue(colunaNome);
                String nome = (valorNome == null ? "" : valorNome.toString()).toLowerCase();
                return nome.startsWith(textoLower);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private TableRowSorter<DefaultTableModel> getOrCreateSorter(JTable table, DefaultTableModel model) {
        if (table.getRowSorter() instanceof TableRowSorter) {
            return (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        return sorter;
    }

    /** Cria um campo de texto estilizado para busca, com texto de dica (placeholder). */
    private JTextField buildCampoBusca() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.putClientProperty("JTextField.placeholderText", "Pesquisar por nome...");
        txt.putClientProperty("JTextField.showClearButton", true);
        txt.setPreferredSize(new Dimension(240, 32));
        txt.setColumns(20);
        return txt;
    }

    /**
     * Cria um DocumentListener que executa a mesma ação para qualquer
     * mudança no campo de texto (inserir, remover ou trocar texto) —
     * evita repetir os 3 métodos do DocumentListener em cada listener.
     */
    private DocumentListener simpleListener(Runnable acao) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { acao.run(); }
            @Override public void removeUpdate(DocumentEvent e)  { acao.run(); }
            @Override public void changedUpdate(DocumentEvent e) { acao.run(); }
        };
    }

    // ----------------------------------------------------------------- helpers
    private DefaultTableModel emptyModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        DashboardPanel.styleTable(t);
        return t;
    }

    /** Colorizes the status column. */
    private void setStatusRenderer(JTable table, int col) {
        table.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, focus, row, c);
                if (!sel && v != null) {
                    String s = v.toString();
                    if (s.contains("Vencido"))      comp.setForeground(Tema.CRITICO_TXT);
                    else if (s.contains("Próx"))    comp.setForeground(Tema.ALERTA_TXT);
                    else                             comp.setForeground(Tema.PRIMARIA_TXT);
                }
                return comp;
            }
        });
    }

    static JButton actionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.putClientProperty("Button.arc", 8);
        btn.putClientProperty("FlatLaf.style", "background: " + toHex(bg) + "; foreground: #ffffff");
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        return btn;
    }

    private static String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}