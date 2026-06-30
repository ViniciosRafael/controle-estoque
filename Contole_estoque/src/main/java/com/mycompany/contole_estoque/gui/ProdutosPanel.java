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
import java.util.ArrayList;
import java.util.List;

// Painel de Produtos — exibe perecíveis e não-perecíveis em abas separadas.
// Permite criar e remover produtos.
public class ProdutosPanel extends JPanel { // Define a classe principal do painel estendendo JPanel

    private static final Color             BTN_BLUE = Tema.PRIMARIA; // Define a cor primária para botões azuis
    private static final Color             BTN_RED  = Tema.CRITICO; // Define a cor vermelha para botões críticos

    private DefaultTableModel perecModel, naoModel, alfabeticaModel; // Declara os modelos de tabela para cada visualização
    private JTable            perecTable, naoTable, alfabeticaTable; // Declara as tabelas que exibirão os dados
    private JLabel            lblTempoOrdenacao; // Declara o rótulo para exibir o tempo gasto na ordenação

    private JTextField txtBuscaPerec, txtBuscaNao, txtBuscaAlfabetica; // Declara os campos de texto para realizar buscas

    private JTabbedPane tabs; // Declara o painel de abas principal
    private JPanel      perecPanel, naoPerecPanel, alfabeticaPanel; // Declara os painéis inseridos dentro de cada aba
    private static final String TITULO_PEREC     = "  Perecíveis  "; // Define o texto da aba de produtos perecíveis
    private static final String TITULO_NAO_PEREC = "  Não Perecíveis  "; // Define o texto da aba de produtos não perecíveis
    private static final String TITULO_ALFABETICA = "  Ordem Alfabética  "; // Define o texto da aba de ordem alfabética

    public ProdutosPanel() { // Define o construtor do painel de produtos
        setBackground(Tema.FUNDO); // Configura a cor de fundo utilizando o tema
        setLayout(new BorderLayout(0, 16)); // Define o gerenciador de layout principal como BorderLayout
        setBorder(new EmptyBorder(28, 28, 28, 28)); // Adiciona um preenchimento interno com bordas vazias
        add(buildHeader(), BorderLayout.NORTH); // Adiciona o painel do cabeçalho na posição norte
        add(buildTabs(),   BorderLayout.CENTER); // Adiciona o painel de abas na posição central
        refresh(); // Atualiza a exibição com os dados mais recentes
    } // Finaliza o construtor

    private JPanel buildHeader() { // Método responsável pela criação do cabeçalho
        JPanel p = new JPanel(new BorderLayout()); // Instancia o painel do cabeçalho com BorderLayout
        p.setOpaque(false); // Define o fundo do painel como transparente

        JLabel title = new JLabel("Produtos"); // Cria o título central
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Configura a fonte, estilo e tamanho do título
        title.setForeground(Tema.TEXTO_TITULO); // Configura a cor do texto baseada no tema
        p.add(title, BorderLayout.WEST); // Adiciona o título à esquerda do cabeçalho

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // Cria um painel com os botões alinhados à direita
        btns.setOpaque(false); // Define o painel de botões como transparente

        JButton btnExportar = actionButton("Exportar p/ Excel", Tema.ACENTO); // Cria o botão de exportação estilizado
        btnExportar.addActionListener(e -> exportarParaExcel()); // Atribui um evento de clique para exportar
        btns.add(btnExportar); // Adiciona o botão de exportar ao painel

        JButton btnNovo = actionButton("+ Novo Produto", BTN_BLUE); // Cria o botão para registrar novos produtos
        btnNovo.addActionListener(e -> { // Atribui evento de clique para o botão
            new NovoProdutoDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true); // Exibe o diálogo de novo produto
            refresh(); // Atualiza as tabelas após fechar o diálogo
        }); // Finaliza o ouvinte do botão
        btns.add(btnNovo); // Adiciona o botão de novo produto ao painel de botões

        p.add(btns, BorderLayout.EAST); // Adiciona o agrupamento de botões no lado direito
        return p; // Retorna o painel do cabeçalho configurado
    } // Finaliza o método buildHeader

    // Abre um seletor de arquivos para o usuário escolher onde salvar o
    // Excel, gera o arquivo com ExportadorExcel (Perecíveis, Não
    // Perecíveis, Estoque/Lotes e Descartes, cada um em sua própria aba)
    // e avisa o resultado.
    private void exportarParaExcel() { // Método de exportação de dados para Excel
        JFileChooser chooser = new JFileChooser(); // Instancia um seletor de diretórios
        chooser.setDialogTitle("Salvar planilha Excel"); // Define o título do seletor
        chooser.setSelectedFile(new java.io.File("estoque.xlsx")); // Determina o nome de arquivo padrão sugerido
        chooser.setFileFilter(new FileNameExtensionFilter("Planilha Excel (*.xlsx)", "xlsx")); // Aplica um filtro para focar em XLSX

        int resultado = chooser.showSaveDialog(this); // Exibe a tela e captura o resultado selecionado
        if (resultado != JFileChooser.APPROVE_OPTION) return; // Aborta a operação se o usuário cancelar

        java.io.File arquivo = chooser.getSelectedFile(); // Obtém a referência do arquivo escolhido
        String caminho = arquivo.getAbsolutePath(); // Extrai o caminho completo em string
        if (!caminho.toLowerCase().endsWith(".xlsx")) { // Verifica se a extensão correta foi fornecida
            caminho += ".xlsx"; // Anexa a extensão se estiver faltando
        } // Finaliza a validação da extensão

        try { // Inicia bloco seguro para capturar exceções
            ExportadorExcel.exportar(caminho); // Processa a exportação passando o caminho
            JOptionPane.showMessageDialog(this, // Mostra notificação em caso de sucesso
                    "Dados exportados com sucesso para:\n" + caminho, // Corpo da mensagem
                    "Exportação concluída", JOptionPane.INFORMATION_MESSAGE); // Título e estilo da mensagem
        } catch (IOException ex) { // Intercepta erros de I/O
            JOptionPane.showMessageDialog(this, // Mostra mensagem de alerta para o erro
                    "Erro ao exportar para Excel:\n" + ex.getMessage(), // Corpo explicativo da falha
                    "Erro na exportação", JOptionPane.ERROR_MESSAGE); // Título da notificação de falha
        } // Encerra o catch
    } // Finaliza o método de exportação

    private JTabbedPane buildTabs() { // Método para montar o componente visual de abas
        tabs = new JTabbedPane(); // Instancia o gerenciador de abas principal
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Seta a fonte das descrições das abas
        // FlatLaf: sublinha a aba selecionada com a cor de destaque // Explicação sobre a visualização via FlatLaf
        tabs.putClientProperty("JTabbedPane.tabAreaAlignment", "leading"); // Alinha as abas à esquerda
        tabs.putClientProperty("JTabbedPane.minimumTabWidth", 120); // Impõe largura mínima para botões das abas

        // ── Perecíveis // Comentário separador para seção de perecíveis
        String[] pCols = { "ID", "Nome", "Categoria", "Preço Unit.", "Est. Mínimo" }; // Cabeçalhos da tabela perecíveis
        perecModel = emptyModel(pCols); // Inicia o modelo utilizando colunas predefinidas
        perecTable = buildTable(perecModel); // Contrói a tabela com as devidas renderizações
        perecTable.getColumnModel().getColumn(0).setPreferredWidth(45); // Especifica a largura para o ID
        perecTable.getColumnModel().getColumn(1).setPreferredWidth(230); // Especifica a largura para o Nome
        perecTable.getColumnModel().getColumn(3).setPreferredWidth(130); // Especifica a largura para o Preço
        perecTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Especifica a largura para Estoque
        txtBuscaPerec = buildCampoBusca(); // Inicializa o campo de busca de perecíveis
        txtBuscaPerec.getDocument().addDocumentListener(simpleListener(this::refreshPerec)); // Adiciona listener para aplicar filtros dinâmicos
        perecPanel = tablePanel(perecTable, perecModel, true, txtBuscaPerec); // Cria o painel completo da aba de perecíveis

        // ── Não Perecíveis // Comentário separador para seção não-perecíveis
        String[] nCols = { "ID", "Nome", "Categoria", "Preço Unitário", "Est. Mínimo" }; // Cabeçalhos de não perecíveis
        naoModel = emptyModel(nCols); // Inicializa o modelo de dados sem registros
        naoTable = buildTable(naoModel); // Constrói o visual da tabela
        naoTable.getColumnModel().getColumn(0).setPreferredWidth(45); // Define a largura para o código identificador
        naoTable.getColumnModel().getColumn(1).setPreferredWidth(220); // Define a largura para a descrição principal
        naoTable.getColumnModel().getColumn(3).setPreferredWidth(130); // Define a largura para o valor unitário
        naoTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Define a largura para a configuração de estoque base
        txtBuscaNao = buildCampoBusca(); // Instancia a barra de pesquisa correspondente
        txtBuscaNao.getDocument().addDocumentListener(simpleListener(this::refreshNaoPerec)); // Acopla atualização automática nas digitações
        naoPerecPanel = tablePanel(naoTable, naoModel, false, txtBuscaNao); // Combina todos os componentes deste segmento em um painel

        // ── Ordem Alfabética (todos os produtos, perecíveis + não perecíveis) // Seção de visualização combinada ordenada
        String[] aCols = { "ID", "Nome", "Categoria", "Tipo" }; // Nomeia as colunas do consolidado
        alfabeticaModel = emptyModel(aCols); // Cria o modelo para o consolidado
        alfabeticaTable = buildTable(alfabeticaModel); // Edifica a tabela para a unificação
        alfabeticaTable.getColumnModel().getColumn(0).setPreferredWidth(45); // Trava largura da coluna de código
        alfabeticaTable.getColumnModel().getColumn(1).setPreferredWidth(230); // Trava largura da coluna nominal
        alfabeticaTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Trava largura da tipologia

        alfabeticaPanel = new JPanel(new BorderLayout(0, 8)); // Aloca um painel principal configurando BorderLayout
        alfabeticaPanel.setOpaque(false); // Retira o preenchimento de fundo
        alfabeticaPanel.setBorder(new EmptyBorder(8, 0, 0, 0)); // Estipula espaçamento superior interno

        JPanel alfabeticaBar = new JPanel(new BorderLayout(8, 0)); // Agrupa controles superiores
        alfabeticaBar.setOpaque(false); // Determina fundo transparente
        txtBuscaAlfabetica = buildCampoBusca(); // Produz barra de pesquisa alfabética
        txtBuscaAlfabetica.getDocument().addDocumentListener(simpleListener(this::refreshOrdemAlfabetica)); // Aciona recarregamento em tempo real
        alfabeticaBar.add(txtBuscaAlfabetica, BorderLayout.WEST); // Alinha barra de procura no lado esquerdo
        lblTempoOrdenacao = new JLabel("Tempo de ordenação: -"); // Etiqueta do cronômetro de ordenação
        lblTempoOrdenacao.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Diminui o texto informacional
        lblTempoOrdenacao.setForeground(Tema.TEXTO_SUB); // Define tom menos contrastante
        alfabeticaBar.add(lblTempoOrdenacao, BorderLayout.EAST); // Põe a informação a direita
        alfabeticaPanel.add(alfabeticaBar, BorderLayout.NORTH); // Introduz painel superior na estrutura principal

        JScrollPane alfabeticaScroll = new JScrollPane(alfabeticaTable); // Insere a tabela em um sistema rolável
        alfabeticaScroll.setBorder(BorderFactory.createEmptyBorder()); // Retira resíduos de moldura
        alfabeticaPanel.add(alfabeticaScroll, BorderLayout.CENTER); // Adiciona no quadrante principal

        atualizarVisibilidadeAbas(); // Recarrega exibição de acordo com restrições

        return tabs; // Emite componente completo
    } // Conclusão de método

    // Adiciona ou remove as abas "Perecíveis" e "Não Perecíveis" conforme a
    // configuração atual em ConfiguracoesStore. Quando um tipo é
    // desabilitado em Configurações, a aba correspondente desaparece daqui;
    // quando reabilitado, a aba volta a aparecer.
    //
    // A aba "Ordem Alfabética" permanece sempre visível, pois ela mostra os
    // produtos cadastrados independentemente do tipo.
    private void atualizarVisibilidadeAbas() { // Procedimento de alternância de visualização das guias
        boolean pereciveisHabilitados    = ConfiguracoesStore.get().isPereciveisHabilitados(); // Checa se perecíveis podem ser vistos
        boolean naoPereciveisHabilitados = ConfiguracoesStore.get().isNaoPereciveisHabilitados(); // Checa permissão visual para não-perecíveis

        // Remove todas as abas e reconstrói na ordem correta — mais simples e
        // confiável do que tentar inserir/remover em posições específicas.
        tabs.removeAll(); // Descarta abas existentes na exibição

        if (pereciveisHabilitados) { // Testa restrição global de perecíveis
            tabs.addTab(TITULO_PEREC, perecPanel); // Adiciona aba somente se validado
        } // Fecha verificação condicional
        if (naoPereciveisHabilitados) { // Testa restrição para itens regulares
            tabs.addTab(TITULO_NAO_PEREC, naoPerecPanel); // Reintroduz caso permitido
        } // Fecha estrutura de condição
        tabs.addTab(TITULO_ALFABETICA, alfabeticaPanel); // Mantém relatório global intacto
    } // Finaliza rotina de reconstrução de abas

    private JPanel tablePanel(JTable table, DefaultTableModel model, boolean isPerec, JTextField campoBusca) { // Sub-rotina de elaboração padrão
        JPanel p = new JPanel(new BorderLayout(0, 8)); // Define recipiente com distanciamento central
        p.setOpaque(false); // Abandona cobertura preenchida
        p.setBorder(new EmptyBorder(8, 0, 0, 0)); // Concede espaçamento em cima

        JPanel bar = new JPanel(new BorderLayout(8, 0)); // Agrega mecanismos na parte de cima
        bar.setOpaque(false); // Fundo livre
        bar.add(campoBusca, BorderLayout.WEST); // Caixa de texto colada à margem esquerda

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // Conjunto para os botões do lado direito
        direita.setOpaque(false); // Preserva fundos transparentes
        JButton btnRem = actionButton("Remover", BTN_RED); // Inicializa botão excludente com fundo vermelho
        btnRem.addActionListener(e -> { // Lida com o processamento do click
            int viewRow = table.getSelectedRow(); // Capta indicação pelo cursor
            if (viewRow < 0) { warn("Selecione um produto."); return; } // Retém andamento se não há indicação
            int modelRow = table.convertRowIndexToModel(viewRow); // Mapeia referencial real do sistema visual
            int id = (int) model.getValueAt(modelRow, 0); // Isola o registro pelo id indexado

            boolean temEstoque = EstoqueStore.get().getLotes().stream() // Varre base corrente do repositório
                    .anyMatch(lote -> lote.getProduto() != null && lote.getProduto().getId() == id); // Exige interrupção caso exista movimentação
            if (temEstoque) { // Detém fluxo perante dependências estritas
                warn("Este produto possui entradas de estoque cadastradas.\n" // Passa feedback preventivo
                        + "Remova ou dê baixa nos lotes dele em \"Estoque\" antes de excluí-lo."); // Indica alternativa
                return; // Paralisa procedimento por integridade
            } // Encerra condicionamento de retenção

            if (JOptionPane.showConfirmDialog(this, "Remover produto selecionado?", // Emite formulário perigoso
                    "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { // Avalia resposta destrutiva
                if (isPerec) EstoqueStore.get().getPerec().removeIf(pp -> pp.getId() == id); // Expulsa base perecível
                else         EstoqueStore.get().getNaoPerec().removeIf(pp -> pp.getId() == id); // Expulsa base imutável
                refresh(); // Comanda atualização das grades afetadas
            } // Expiração da caixa
        }); // Desfaz a instrução do clique
        direita.add(btnRem); // Ajusta o controle gerado
        bar.add(direita, BorderLayout.EAST); // Conecta sub-container ao conjunto superior
        p.add(bar, BorderLayout.NORTH); // Consolida faixa de utilidades

        JScrollPane sp = new JScrollPane(table); // Embala dados tabulares numa área com barra de deslocamento
        sp.setBorder(BorderFactory.createEmptyBorder()); // Arranca molduras padrão de JScroll
        sp.getViewport().setBackground(Tema.FUNDO); // Fixa matiz de espaço vazio com cor universal
        p.add(sp, BorderLayout.CENTER); // Integra visualização preenchendo áreas abertas
        return p; // Libera peça estrutural pronta
    } // Arremate da formatação padrão

    // ----------------------------------------------------------------- refresh // Limite informacional de setorização
    void refresh() { // Orquestrador geral de atualizações sistêmicas
        atualizarVisibilidadeAbas(); // Reconstrói exibições segundo preferências
        refreshPerec(); // Dispara sincronismo para orgânicos
        refreshNaoPerec(); // Dispara sincronismo para prateleiras
        refreshOrdemAlfabetica(); // Executa recalculo em árvore consolidada
    } // Limite final

    private void refreshPerec() { // Algoritmo de renovação orgânica
        perecModel.setRowCount(0); // Liquida todas as tuplas ativas
        for (ProdutoPerecivel p : EstoqueStore.get().getPerec()) { // Itera toda base persistida compatível
            perecModel.addRow(new Object[] { // Preenche nova estrutura matricial
                    p.getId(), p.getNome().toUpperCase(), p.getCategoria(), // Agrupa identificação baseada em Strings limpas
                    String.format("R$ %.2f", p.getPrecoUnitario()), // Condiciona máscara financeira
                    p.getEstoqueMinimo() // Extrai limiar perigoso configurado
            }); // Trava linha no vetor
        } // Conclui inserções sequenciais
        aplicarFiltro(perecTable, txtBuscaPerec, 1); // Submete coleção processada pela filtragem contextual ativa
    } // Final da sincronização

    private void refreshNaoPerec() { // Sincronização secundária
        naoModel.setRowCount(0); // Aplica destruição total ao visual
        for (ProdutoNaoPerecivel p : EstoqueStore.get().getNaoPerec()) { // Explora banco associado à classe
            naoModel.addRow(new Object[]{ // Injete uma matriz sequencial
                p.getId(), p.getNome().toUpperCase(), p.getCategoria(), // Apresenta texto nominal processado
                String.format("R$ %.2f", p.getPrecoUnitario()), // Normaliza pontuação financeira
                p.getEstoqueMinimo() // Reflete limite logístico de suprimento
            }); // Limite fechado
        } // Encapsulamento
        aplicarFiltro(naoTable, txtBuscaNao, 1); // Reinjeta os restritores textuais após repopular
    } // Bloqueio lógico de fim

    // Junta perecíveis e não perecíveis numa única lista, ordena por nome
    // (mantendo o ID de cadastro de cada produto), mede o tempo gasto na
    // ordenação e popula a tabela da aba "Ordem Alfabética".
    private void refreshOrdemAlfabetica() { // Monta grade universal lexicograficamente
        List<Produto> todos = new ArrayList<>(); // Instancia contêiner provisório
        todos.addAll(EstoqueStore.get().getPerec()); // Contribui com as primeiras partições
        todos.addAll(EstoqueStore.get().getNaoPerec()); // Soma os elementos faltantes para consolidação

        OrdenadorProdutosPorNome.ResultadoOrdenacaoPorNome<Produto> resultado = // Solicita a estatística da utilidade externa
                OrdenadorProdutosPorNome.ordenarPorNomeComTempo(todos); // Envia contêiner sujo ao processador temporal

        alfabeticaModel.setRowCount(0); // Retoma estrutura da tabela principal vazia
        for (Produto p : resultado.getProdutosOrdenados()) { // Para cada artefato já estruturado
            String tipo = (p instanceof ProdutoPerecivel) ? "Perecível" : "Não Perecível"; // Extrai a instância e define texto amigável
            alfabeticaModel.addRow(new Object[]{ // Monta os objetos de formatação no framework
                    p.getId(), p.getNome().toUpperCase(), p.getCategoria(), tipo // Passa dados fundamentais ordenados
            }); // Finaliza composição singular
        } // Encerramento laço de injeção
        aplicarFiltro(alfabeticaTable, txtBuscaAlfabetica, 1); // Passa regulação local após preenchimento

        lblTempoOrdenacao.setText(String.format( // Redefine caixa textual baseada no processo
                "Tempo de ordenação: %.3f ms (%d produtos)  •  Comparações: %,d  •  Trocas: %,d", // Gabarito com marcações
                resultado.getTempoMilissegundos(), todos.size(), // Expõe grandezas temporais baseadas
                resultado.getComparacoes(), resultado.getTrocas())); // Submete estatística fina ao rodapé
    } // Delimitação do agrupamento

    // Filtra as linhas da tabela mantendo apenas as que COMEÇAM com o texto
    // digitado no campo de busca — considerando tanto o ID (coluna 0) quanto
    // o nome do produto (coluna colunaNome). A busca não diferencia
    // maiúsculas de minúsculas.
    //
    // Exemplos: digitar "alf" encontra "Alface" (pelo nome); digitar "52"
    // encontra o produto com ID 52, ou ID 520, 521... (pelo ID); digitar "q"
    // encontra "Queijo" mas NÃO "Requeijão" (não começa com "q").
    //
    // Usa RowFilter, que filtra apenas a exibição (a tabela
    // continua mostrando os dados reais do model por trás).
    private void aplicarFiltro(JTable table, JTextField campoBusca, int colunaNome) { // Atua como despachante para RowSorter
        DefaultTableModel model = (DefaultTableModel) table.getModel(); // Isola dependência de modelo

        // Tabelas grandes (ex.: 50.000 linhas) precisam de um TableRowSorter
        // já associado ao model; criamos um apenas uma vez por tabela.
        TableRowSorter<DefaultTableModel> sorter = getOrCreateSorter(table, model); // Reaproveita sistema indexado para performance

        String texto = campoBusca.getText().trim(); // Isola ruído lateral proveniente da digitação
        if (texto.isEmpty()) { // Teste inicial para zerar condicionantes
            sorter.setRowFilter(null); // Zera e libera todas as visões preenchidas
            return; // Evasão prematura por suficiência
        } // Encerramento condicional nulo

        String textoLower = texto.toLowerCase(); // Aplica case-folding simplificado ao restritor
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() { // Define uma classe anônima instanciadora
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) { // Validador de exibição unitária
                // Coluna 0 = ID (número) — compara como texto, ex: "52" casa com ID 52, 520, 521...
                String id = String.valueOf(entry.getValue(0)); // Projeta valores indexados na dimensão de strings
                if (id.startsWith(textoLower)) return true; // Confere match posicional inicial e cede o passo

                // Coluna colunaNome = Nome do produto
                Object valorNome = entry.getValue(colunaNome); // Coleta alvo semântico de preposição
                String nome = (valorNome == null ? "" : valorNome.toString()).toLowerCase(); // Exige formato seguro com minúsculas convertidas
                return nome.startsWith(textoLower); // Executa verificação restritiva com resposta direta
            } // Encerra avaliação pontual
        }); // Associa e atualiza engine do sorter atrelado
    } // Finda roteamento visual com restrições

    @SuppressWarnings("unchecked") // Omite desvios detectáveis referentes às conversões sem garantia forte de tipos
    private TableRowSorter<DefaultTableModel> getOrCreateSorter(JTable table, DefaultTableModel model) { // Gestor de estados de sorters
        if (table.getRowSorter() instanceof TableRowSorter) { // Busca alocação original vinculada na matriz
            return (TableRowSorter<DefaultTableModel>) table.getRowSorter(); // Emite estrutura persistente resgatada
        } // Encerra fluxo otimista
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model); // Efetiva alocação de construtor caro
        table.setRowSorter(sorter); // Prega a nova alocação atrelada diretamente
        return sorter; // Exporta recém-criada
    } // Final

    // Cria um campo de texto estilizado para busca, com texto de dica (placeholder).
    private JTextField buildCampoBusca() { // Instancia utilidade modular padrão de form inputs
        JTextField txt = new JTextField(); // Requer campo limpo
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Personaliza render de vetores tipográficos
        txt.putClientProperty("JTextField.placeholderText", "Pesquisar por nome..."); // Incorpora marca d'água via hack FlatLaf
        txt.putClientProperty("JTextField.showClearButton", true); // Embuti atalho de retrocesso automático na caixa
        txt.setPreferredSize(new Dimension(240, 32)); // Delimita e trava fronteira dimensional
        txt.setColumns(20); // Impõe número restritivo para aproximação horizontal
        return txt; // Emite form preenchido
    } // Encerramento do método modular

    // Cria um DocumentListener que executa a mesma ação para qualquer
    // mudança no campo de texto (inserir, remover ou trocar texto) —
    // evita repetir os 3 métodos do DocumentListener em cada listener.
    private DocumentListener simpleListener(Runnable acao) { // Engloba chamadas múltiplas sem overhead
        return new DocumentListener() { // Exige listener instanciado na memória virtual
            @Override public void insertUpdate(DocumentEvent e)  { acao.run(); } // Direciona ao delegador quando injetado
            @Override public void removeUpdate(DocumentEvent e)  { acao.run(); } // Direciona ao delegador após anulação parcial
            @Override public void changedUpdate(DocumentEvent e) { acao.run(); } // Comanda execução genérica atrelada à formatações
        }; // Expirar declaração
    } // Retorna encerramento do delegate

    private DefaultTableModel emptyModel(String[] cols) { // Forja representação segura para evitar falhas gráficas iniciais
        return new DefaultTableModel(cols, 0) { // Envia colunas base ignorando injeção em massa
            @Override public boolean isCellEditable(int r, int c) { return false; } // Substitui flag para proibir interações indesejadas
        }; // Fim sobrecarga abstrata
    } // Desvincula método utilitário

    private JTable buildTable(DefaultTableModel model) { // Central de polimento de grades
        JTable table = new JTable(model); // Prepara recipiente visual
        DashboardPanel.styleTable(table); // Repassa controle estilístico a componentes centrais
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() { // Interceptador gráfico de exibições unitárias
            @Override public Component getTableCellRendererComponent( // Início da máquina de renderização
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) { // Aceita contexto espacial posicional em massa
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col); // Invocação legada para prover fundações
                if (!sel) { // Discrimina cenários base
                    c.setBackground(Tema.FUNDO); // Regula colorização inativa
                    c.setForeground(Tema.TEXTO_TITULO); // Regula cores primárias ativas
                } else { // Determina condição oposta visual
                    c.setBackground(new Color(0, 120, 210)); // Estipula tons focais pesados
                    c.setForeground(Color.WHITE); // Ilumina elementos no escuro do fundo focal
                } // Fecha desvio de estado visual da tabela
                return c; // Emite peça visual alterada
            } // Expira lógica construtora
        }); // Final de injeção em componente
        return table; // Repassa interface pronta
    } // Finalização do componente de tabela principal

    static JButton actionButton(String text, Color bg) { // Função globalizável para criação de botões padronizados
        JButton btn = new JButton(text); // Estruturação instanciada contendo texto pré-aprovado
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Insere peso considerável no título provido
        btn.setBackground(bg); // Emprega tom providenciado via método
        btn.setForeground(Color.WHITE); // Efetua lavagem das tintas das letras
        btn.setOpaque(true); // Exige cor aplicada cobrindo integralmente as bordas
        btn.putClientProperty("Button.arc", 8); // Dobra os cantos via look-and-feel alternativo
        btn.putClientProperty("FlatLaf.style", "background: " + toHex(bg) + "; foreground: #ffffff"); // Assegura comportamento consistente sobrepondo configurações FlatLaf
        btn.setBorderPainted(false); // Retira linhas contínuas laterais nativas do java swing
        btn.setFocusPainted(false); // Remove retângulo interno após receber o foco de interação do teclado
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Ativa o design dinâmico alterando ponteiro de mouse
        btn.setBorder(new EmptyBorder(10, 16, 10, 16)); // Expande tamanho global adicionando caixas invisíveis em redor
        return btn; // Retorna instância moldada com a finalidade de compor interfaces
    } // Finaliza construtor genérico

    private static String toHex(Color c) { // Pequeno resolvedor de transformação base gráfica para strings hexadecimais web
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()); // Empacota RGB puros utilizando interpolação em estilo C clássico
    } // Devolve para chamada global

    private void warn(String msg) { // Exibe diálogos restritivos à interface encapsulada
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE); // Gera interrupção amarrada ao frame local
    } // Fim da declaração
} // Fim do documento java ProdutosPanel