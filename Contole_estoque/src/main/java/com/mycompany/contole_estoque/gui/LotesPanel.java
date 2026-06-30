package com.mycompany.contole_estoque.gui; // Declara o pacote da classe

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.dialogs.*;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Painel de Estoque — lista entradas de estoque de TODOS os produtos
// (perecíveis e não perecíveis). Permite criar novas entradas, dar baixa e descartar.
//
// Correções aplicadas:
// - Adicionado botão "Descartar" que abre o NovoDescarteDialog pré-selecionando o lote.
// - Corrigida a seleção de linha para usar o índice do modelo (não da view),
//   evitando erros quando a tabela está ordenada/filtrada.
public class LotesPanel extends JPanel { // Declara a classe LotesPanel estendendo JPanel

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Define o formato de data padrão

    private DefaultTableModel        model; // Declara o modelo da tabela
    private JTable                   table; // Declara a tabela visual
    private JTextField               txtBusca; // Declara o campo de texto para busca
    private TableRowSorter<DefaultTableModel> sorter; // Declara o classificador de linhas da tabela

    public LotesPanel() { // Construtor da classe
        setBackground(Tema.FUNDO); // Define a cor de fundo do painel
        setLayout(new BorderLayout(0, 16)); // Define o layout como BorderLayout com espaçamento vertical
        setBorder(new EmptyBorder(28, 28, 28, 28)); // Define as margens internas do painel

        // Painel central: barra de busca + tabela
        JPanel center = new JPanel(new BorderLayout(0, 8)); // Cria o painel central com BorderLayout
        center.setOpaque(false); // Torna o painel central transparente
        center.add(buildSearchBar(), BorderLayout.NORTH); // Adiciona a barra de busca no topo do painel central
        center.add(buildTable(),     BorderLayout.CENTER); // Adiciona a tabela no centro do painel central

        add(buildHeader(), BorderLayout.NORTH); // Adiciona o cabeçalho no topo do painel principal
        add(center,        BorderLayout.CENTER); // Adiciona o painel central no centro do painel principal
        add(buildLegend(), BorderLayout.SOUTH); // Adiciona a legenda na parte inferior do painel principal
        refresh(); // Atualiza os dados exibidos na tela
    } // Fim do construtor

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() { // Método para construir o cabeçalho
        JPanel p = new JPanel(new BorderLayout()); // Cria o painel do cabeçalho com BorderLayout
        p.setOpaque(false); // Torna o painel transparente

        JLabel title = new JLabel("Estoque"); // Cria o rótulo do título
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Define a fonte do título
        title.setForeground(Tema.TEXTO_TITULO); // Define a cor do texto do título
        p.add(title, BorderLayout.WEST); // Adiciona o título à esquerda do cabeçalho

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // Cria um painel para os botões alinhado à direita
        btns.setOpaque(false); // Torna o painel de botões transparente

        JButton btnBaixa = ProdutosPanel.actionButton("Dar Baixa", new Color(200, 125, 0)); // Cria o botão de dar baixa com cor específica
        btnBaixa.addActionListener(e -> { // Adiciona um ouvinte de ação ao botão
            LoteEstoque lote = getLoteSelecionado(); // Obtém o lote selecionado
            if (lote == null) { warn("Selecione um item do estoque."); return; } // Exibe aviso e retorna se nenhum lote foi selecionado
            new DarBaixaDialog((JFrame) SwingUtilities.getWindowAncestor(this), lote).setVisible(true); // Abre a janela para dar baixa
            EstoqueStore.get().gerarAlertas(); // Gera alertas de estoque
            refresh(); // Atualiza os dados exibidos
        }); // Fim do ouvinte de ação

        JButton btnDescartar = ProdutosPanel.actionButton("Descartar", new Color(185, 50, 50)); // Cria o botão de descartar com cor específica
        btnDescartar.addActionListener(e -> { // Adiciona um ouvinte de ação ao botão
            LoteEstoque lote = getLoteSelecionado(); // Obtém o lote selecionado
            if (lote == null) { warn("Selecione um item do estoque."); return; } // Exibe aviso e retorna se nenhum lote foi selecionado
            if (lote.getProduto() == null) { // Verifica se há um produto associado
                warn("Este lote não possui produto associado e não pode ser descartado."); // Exibe aviso se não houver produto associado
                return; // Retorna para abortar a ação
            } // Fim da verificação de produto nulo
            NovoDescarteDialog dlg = new NovoDescarteDialog( // Cria o diálogo de novo descarte
                    (JFrame) SwingUtilities.getWindowAncestor(this), lote); // Passa a janela pai e o lote
            dlg.setVisible(true); // Exibe o diálogo de descarte
            EstoqueStore.get().gerarAlertas(); // Gera alertas de estoque após fechar
            refresh(); // Atualiza a tela
        }); // Fim do ouvinte de ação

        JButton btnNovo = ProdutosPanel.actionButton("+ Nova Entrada", new Color(0, 120, 210)); // Cria o botão de nova entrada com cor específica
        btnNovo.addActionListener(e -> { // Adiciona um ouvinte de ação ao botão
            if (EstoqueStore.get().getPerec().isEmpty() // Verifica se a lista de perecíveis está vazia
                    && EstoqueStore.get().getNaoPerec().isEmpty()) { // Verifica se a lista de não perecíveis está vazia
                warn("Cadastre pelo menos um produto antes de registrar uma entrada."); // Exibe aviso se não houver produtos
                return; // Retorna para abortar a ação
            } // Fim da verificação de produtos vazios
            new NovoLoteDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true); // Abre a janela de novo lote
            EstoqueStore.get().gerarAlertas(); // Gera alertas de estoque
            refresh(); // Atualiza os dados da tela
        }); // Fim do ouvinte de ação

        btns.add(btnBaixa); // Adiciona o botão dar baixa ao painel de botões
        btns.add(btnDescartar); // Adiciona o botão descartar ao painel de botões
        btns.add(btnNovo); // Adiciona o botão nova entrada ao painel de botões
        p.add(btns, BorderLayout.EAST); // Adiciona o painel de botões à direita do cabeçalho
        return p; // Retorna o painel do cabeçalho
    } // Fim do método buildHeader

    // Retorna o LoteEstoque correspondente à linha selecionada na tabela,
    // convertendo corretamente o índice da view para o índice do modelo.
    private LoteEstoque getLoteSelecionado() { // Método para obter o lote selecionado na tabela
        int viewRow = table.getSelectedRow(); // Obtém o índice da linha selecionada na visualização da tabela
        if (viewRow < 0) return null; // Retorna nulo se nenhuma linha estiver selecionada
        int modelRow = table.convertRowIndexToModel(viewRow); // Converte o índice da visualização para o índice do modelo de dados
        return EstoqueStore.get().getLotes().get(modelRow); // Retorna o lote correspondente ao índice do modelo
    } // Fim do método getLoteSelecionado

    // Barra de busca em tempo real — filtra por lote, produto ou tipo.
    private JPanel buildSearchBar() { // Método para construir a barra de pesquisa
        JPanel bar = new JPanel(new BorderLayout(8, 0)); // Cria o painel da barra com espaçamento horizontal
        bar.setOpaque(false); // Torna a barra de pesquisa transparente

        txtBusca = new JTextField(); // Inicializa o campo de texto de busca
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Define a fonte do campo de texto
        txtBusca.putClientProperty("JTextField.placeholderText", "Pesquisar por produto, lote ou tipo..."); // Adiciona texto de placeholder (dica)
        txtBusca.putClientProperty("JTextField.showClearButton", true); // Adiciona um botão de limpar no campo
        txtBusca.setPreferredSize(new Dimension(300, 32)); // Define as dimensões preferidas do campo
        txtBusca.getDocument().addDocumentListener(new DocumentListener() { // Adiciona um ouvinte para detectar alterações no texto
            @Override public void insertUpdate(DocumentEvent e)  { SwingUtilities.invokeLater(() -> aplicarFiltro()); } // Aplica filtro ao inserir texto
            @Override public void removeUpdate(DocumentEvent e)  { SwingUtilities.invokeLater(() -> aplicarFiltro()); } // Aplica filtro ao remover texto
            @Override public void changedUpdate(DocumentEvent e) {} // Método vazio exigido pela interface
        }); // Fim do ouvinte do documento

        bar.add(txtBusca, BorderLayout.WEST); // Adiciona o campo de texto à esquerda da barra
        return bar; // Retorna o painel da barra de pesquisa
    } // Fim do método buildSearchBar

    // Aplica o filtro de texto no sorter da tabela.
    private void aplicarFiltro() { // Método para aplicar filtro de pesquisa na tabela
        String termo = txtBusca.getText().trim().toLowerCase(); // Pega o texto da busca, remove espaços e converte para minúsculas
        if (termo.isEmpty()) { // Verifica se o termo está vazio
            sorter.setRowFilter(null); // Remove qualquer filtro da tabela se o texto for vazio
            return; // Encerra a execução do método
        } // Fim da verificação do termo vazio
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() { // Cria e aplica um novo filtro de linhas baseado no termo
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) { // Método para verificar se a linha deve ser incluída
                // Colunas: 0=Lote, 1=Produto, 2=Tipo, 3=Qtd, 4=Est.Min, 5=Data, 6=Status
                for (int col : new int[]{0, 1, 2, 6}) { // Itera apenas sobre colunas específicas (Lote, Produto, Tipo, Status)
                    Object v = entry.getValue(col); // Obtém o valor da célula na coluna atual
                    if (v != null && v.toString().toLowerCase().contains(termo)) return true; // Verifica se o valor contém o termo pesquisado e retorna verdadeiro
                } // Fim do laço de repetição das colunas
                return false; // Retorna falso se o termo não for encontrado em nenhuma coluna
            } // Fim do método include
        }); // Fim da definição do filtro
    } // Fim do método aplicarFiltro

    private JScrollPane buildTable() { // Método para construir a tabela
        String[] cols = {"Lote", "Produto", "Tipo", "Qtd Atual", "Est. Min.", "Data Entrada", "Validade / Status"}; // Define os nomes das colunas
        model = new DefaultTableModel(cols, 0) { // Cria o modelo da tabela com as colunas e sem linhas
            @Override public boolean isCellEditable(int r, int c) { return false; } // Define que as células não podem ser editadas pelo usuário
        }; // Fim da definição do modelo

        table = new JTable(model); // Cria a tabela usando o modelo configurado
        DashboardPanel.styleTable(table); // Aplica estilo visual customizado à tabela
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Define a largura preferida da coluna 0 (Lote)
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Define a largura preferida da coluna 1 (Produto)
        table.getColumnModel().getColumn(2).setPreferredWidth(110); // Define a largura preferida da coluna 2 (Tipo)
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // Define a largura preferida da coluna 3 (Quantidade)
        table.getColumnModel().getColumn(4).setPreferredWidth(80); // Define a largura preferida da coluna 4 (Estoque Mínimo)
        table.getColumnModel().getColumn(5).setPreferredWidth(110); // Define a largura preferida da coluna 5 (Data de Entrada)
        table.getColumnModel().getColumn(6).setPreferredWidth(150); // Define a largura preferida da coluna 6 (Status)

        // Permite ordenação clicando no cabeçalho e filtragem via barra de busca
        sorter = new TableRowSorter<>(model); // Cria o ordenador de linhas para a tabela
        table.setRowSorter(sorter); // Associa o ordenador à tabela

        // coloriza linha inteira com base no status da última coluna
        TableCellRenderer colorRenderer = new DefaultTableCellRenderer() { // Cria um renderizador customizado para colorir as linhas
            @Override public Component getTableCellRendererComponent( // Método para renderizar a célula
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) { // Parâmetros do renderizador
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col); // Chama a implementação padrão do renderizador
                if (!sel) { // Verifica se a linha não está selecionada
                    int modelRow = t.convertRowIndexToModel(row); // Converte o índice visual para índice de modelo
                    String st = (String) model.getValueAt(modelRow, 6); // Obtém o status na última coluna
                    c.setBackground(Tema.FUNDO); // Define a cor de fundo padrão
                    if (st != null && st.contains("Vencido")) // Verifica se o status é de vencido
                        c.setForeground(Tema.CRITICO_TXT); // Define a cor do texto para crítico se vencido
                    else if (st != null && st.contains("Prox")) // Verifica se o status é próximo ao vencimento
                        c.setForeground(Tema.ALERTA_TXT); // Define a cor do texto para alerta se próximo de vencer
                    else // Caso não caia nas condições anteriores
                        c.setForeground(Tema.PRIMARIA_TXT); // Define a cor do texto padrão
                } // Fim da condição de seleção
                return c; // Retorna o componente renderizado
            } // Fim do método getTableCellRendererComponent
        }; // Fim da classe anônima DefaultTableCellRenderer
        for (int i = 0; i < cols.length; i++) // Itera sobre todas as colunas
            table.getColumnModel().getColumn(i).setCellRenderer(colorRenderer); // Aplica o renderizador em cada coluna

        JScrollPane sp = new JScrollPane(table); // Adiciona a tabela em um painel de rolagem
        sp.setBorder(BorderFactory.createEmptyBorder()); // Remove as bordas do painel de rolagem
        sp.getViewport().setBackground(Tema.FUNDO); // Define a cor de fundo da área visível do painel
        return sp; // Retorna o painel de rolagem com a tabela
    } // Fim do método buildTable

    private JPanel buildLegend() { // Método para construir a área de legenda
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4)); // Cria o painel de legenda alinhado à esquerda
        p.setOpaque(false); // Torna o painel da legenda transparente
        legend(p, "OK",                           Tema.PRIMARIA_TXT); // Adiciona item na legenda para OK
        legend(p, "Próximo ao Vencimento (<=5 d)", Tema.ALERTA_TXT); // Adiciona item na legenda para Próximo ao Vencimento
        legend(p, "Vencido",                       Tema.CRITICO_TXT); // Adiciona item na legenda para Vencido
        legend(p, "Não Perecível: sem vencimento", Tema.PRIMARIA_TXT); // Adiciona item na legenda para Não Perecível
        return p; // Retorna o painel de legenda
    } // Fim do método buildLegend

    private void legend(JPanel p, String text, Color c) { // Método para criar um item individual da legenda
        JLabel l = new JLabel(text); // Cria o rótulo da legenda
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Define a fonte da legenda
        l.setForeground(c); // Define a cor do texto da legenda
        p.add(l); // Adiciona o rótulo ao painel da legenda
    } // Fim do método legend

    // ----------------------------------------------------------------- refresh
    public void refresh() { // Método para recarregar e exibir dados na tabela
        EstoqueStore.get().limparLotesZerados(); // Remove os lotes com quantidade zero do estoque
        model.setRowCount(0); // Zera a quantidade de linhas da tabela para atualizar
        for (LoteEstoque lote : EstoqueStore.get().getLotes()) { // Laço para percorrer todos os lotes em estoque
            Produto prod = lote.getProduto(); // Pega a referência do produto contido no lote

            if (prod == null) { // Verifica se o produto não existe
                model.addRow(new Object[]{ // Adiciona uma nova linha à tabela com os dados ausentes
                    lote.getNumeroLote(), // Obtém o número do lote
                    "(produto removido)", // Informa que o produto não foi encontrado
                    "—", // Preenche coluna vazia
                    lote.getQuantidade(), // Mostra a quantidade de lote
                    "—", // Preenche coluna vazia
                    lote.getDataEntrada().format(FMT), // Exibe a data formatada
                    "—" // Preenche coluna vazia
                }); // Fim da adição da linha
                continue; // Pula para a próxima iteração do laço
            } // Fim da verificação de produto nulo

            String tipo, statusCol; // Declara as variáveis tipo e status

            if (prod instanceof ProdutoPerecivel) { // Verifica se o produto é do tipo perecível
                tipo = "Perecivel"; // Define o texto do tipo como perecível
                int dias = lote.diasParaVencer(); // Calcula a quantidade de dias para o vencimento
                if (lote.isVencido())  statusCol = "Vencido"; // Se estiver vencido, atualiza o status correspondente
                else if (dias <= 5)    statusCol = "Prox. Venc. (" + dias + "d)"; // Se perto do vencimento, atualiza o status correspondente
                else { // Caso não esteja vencido nem próximo
                    String valStr = lote.getDataValidade() != null // Verifica se existe data de validade
                        ? lote.getDataValidade().format(FMT) : "—"; // Formata a data se existir ou deixa traço
                    statusCol = "OK  val: " + valStr; // Monta o texto de status indicando OK e a data
                } // Fim do condicional else
            } else { // Caso seja um produto não perecível
                tipo      = "Nao Perecivel"; // Define a string de tipo
                statusCol = "OK  (sem vencimento)"; // Define o status como não vencível
            } // Fim da estrutura condicional if-else

            model.addRow(new Object[]{ // Adiciona uma linha na tabela com as informações completas
                lote.getNumeroLote(), // Número do lote
                prod.getNome().toUpperCase(), // Nome do produto em maiúsculas
                tipo, // Tipo do produto
                lote.getQuantidade(), // Quantidade atual
                prod.getEstoqueMinimo(), // Estoque mínimo estabelecido
                lote.getDataEntrada().format(FMT), // Data formatada de entrada
                statusCol // Status da mercadoria
            }); // Finaliza a adição da linha na tabela
        } // Fim da iteração de lotes
    } // Fim do método refresh

    private void warn(String msg) { // Método para exibir mensagens de aviso
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE); // Mostra a caixa de diálogo com o aviso
    } // Fim do método warn
} // Fim da classe LotesPanel