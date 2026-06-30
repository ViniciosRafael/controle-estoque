package com.mycompany.contole_estoque.gui; // Define o pacote ao qual esta classe pertence

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import com.mycompany.contole_estoque.gui.theme.Tema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

// Painel de Histórico de Movimentações — exibe todas as movimentações do estoque:
// inclusões, baixas e descartes.
//
// Também exibe o total de prejuízo acumulado (calculado apenas sobre os descartes).
public class DescartesPanel extends JPanel { // Declara a classe DescartesPanel herdando as propriedades de um JPanel

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cria uma constante formatadora de data no padrão brasileiro

    private DefaultTableModel model; // Declara a variável que guardará o modelo de dados da tabela
    private JLabel            lblTotal; // Declara a variável que guardará o texto do total
    private JComboBox<String> cbFiltro; // Declara a variável que representará a lista suspensa de filtros

    public DescartesPanel() { // Método construtor principal do painel de descartes
        setBackground(Tema.FUNDO); // Define a cor de fundo utilizando a cor estipulada no Tema
        setLayout(new BorderLayout(0, 16)); // Configura o layout como BorderLayout com espaçamento vertical de 16 pixels
        setBorder(new EmptyBorder(28, 28, 28, 28)); // Insere uma margem transparente de 28 pixels em todas as bordas
        add(buildHeader(), BorderLayout.NORTH); // Posiciona o painel do cabeçalho na parte superior da tela
        add(buildTable(),  BorderLayout.CENTER); // Posiciona o painel contendo a tabela na região central da tela
        add(buildFooter(), BorderLayout.SOUTH); // Posiciona o painel de rodapé na região inferior da tela
        refresh(); // Aciona o método para preencher a tabela com os dados iniciais
    } // Encerra o método construtor

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() { // Método que constrói e retorna o painel do cabeçalho
        JPanel p = new JPanel(new BorderLayout()); // Instancia um novo painel utilizando BorderLayout
        p.setOpaque(false); // Remove a opacidade para adotar a cor de fundo do painel pai

        JLabel title = new JLabel("Histórico de Movimentações"); // Instancia um novo componente de texto para o título
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Altera a fonte do título para Segoe UI em negrito tamanho 24
        title.setForeground(Tema.TEXTO_TITULO); // Ajusta a cor do texto do título usando a cor do Tema
        p.add(title, BorderLayout.WEST); // Adiciona o título no lado esquerdo do painel do cabeçalho

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // Cria um painel para abrigar os controles, com alinhamento à direita
        controls.setOpaque(false); // Define este painel de controles como transparente também

        JLabel lblFiltro = new JLabel("Filtrar:"); // Cria o rótulo escrito "Filtrar:"
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Define a fonte deste rótulo como normal e tamanho 12
        lblFiltro.setForeground(Tema.TEXTO_SUB); // Ajusta a cor do rótulo para a cor secundária do tema
        controls.add(lblFiltro); // Adiciona este rótulo dentro do painel de controles

        cbFiltro = new JComboBox<>(new String[]{"Todos", "Inclusão", "Baixa", "Descarte"}); // Inicia a caixa de seleção carregando as quatro opções
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Configura a fonte da caixa de seleção
        cbFiltro.addActionListener(e -> refresh()); // Cadastra um evento para que a tabela seja atualizada sempre que o filtro for modificado
        controls.add(cbFiltro); // Adiciona a caixa de seleção finalizada dentro do painel de controles

        p.add(controls, BorderLayout.EAST); // Põe o painel de controles no lado direito do cabeçalho
        return p; // Retorna o cabeçalho inteiramente montado
    } // Finaliza o método de construção do cabeçalho

    private JScrollPane buildTable() { // Método encarregado de criar a tabela e sua barra de rolagem
        String[] cols = {"ID", "Tipo", "Produto", "Lote", "Quantidade", "Data", "Observação / Motivo", "Prejuízo"}; // Define num vetor de texto o nome de cada coluna
        model = new DefaultTableModel(cols, 0) { // Cria o modelo da tabela passando o nome das colunas e iniciando sem linhas
            @Override public boolean isCellEditable(int r, int c) { return false; } // Substitui o método de edição para sempre retornar falso, bloqueando a digitação
        }; // Fim da declaração da classe anônima do modelo

        JTable table = new JTable(model); // Cria o componente tabela, associado ao modelo que criamos
        DashboardPanel.styleTable(table); // Aplica a formatação visual padrão do projeto na tabela
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // Fixa a largura preferencial da coluna ID em 40 pixels
        table.getColumnModel().getColumn(1).setPreferredWidth(90); // Fixa a largura preferencial da coluna Tipo em 90 pixels
        table.getColumnModel().getColumn(2).setPreferredWidth(190); // Fixa a largura preferencial da coluna Produto em 190 pixels
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Fixa a largura preferencial da coluna Lote em 100 pixels
        table.getColumnModel().getColumn(4).setPreferredWidth(80); // Fixa a largura preferencial da coluna Quantidade em 80 pixels
        table.getColumnModel().getColumn(5).setPreferredWidth(90); // Fixa a largura preferencial da coluna Data em 90 pixels
        table.getColumnModel().getColumn(6).setPreferredWidth(220); // Fixa a largura preferencial da coluna Observação em 220 pixels
        table.getColumnModel().getColumn(7).setPreferredWidth(100); // Fixa a largura preferencial da coluna Prejuízo em 100 pixels

        // Coloriza as linhas conforme o tipo de movimentação
        TableCellRenderer tipoRenderer = new DefaultTableCellRenderer() { // Constrói um renderizador personalizado para repintar as células
            @Override public Component getTableCellRendererComponent( // Início do método que é chamado para pintar cada célula
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) { // Recebe os dados de estado atual da célula
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col); // Recupera o renderizador padrão como ponto de partida
                if (!sel) { // Avalia se a linha avaliada NÃO está selecionada pelo usuário
                    c.setBackground(Tema.FUNDO); // Atribui a cor de fundo padrão do painel
                    int modelRow = t.convertRowIndexToModel(row); // Busca o índice exato da linha no modelo (ignora a ordenação visual)
                    String tipo = (String) model.getValueAt(modelRow, 1); // Lê o texto da coluna 1 correspondente ao tipo
                    if ("Descarte".equals(tipo)) // Verifica se esse tipo de movimentação é um Descarte
                        c.setForeground(Tema.CRITICO_TXT); // Modifica a fonte para cor vermelha de crítica
                    else if ("Baixa".equals(tipo)) // Verifica se o tipo da movimentação é Baixa
                        c.setForeground(Tema.ALERTA_TXT); // Modifica a fonte para cor laranja de alerta
                    else // Se não for descarte nem baixa, pressupõe ser Inclusão
                        c.setForeground(Tema.PRIMARIA_TXT); // Modifica a fonte para azul primário
                } else { // Caso a linha atual estiver destacada/selecionada
                    c.setBackground(new Color(0, 120, 210)); // Define a cor de fundo de seleção azul escuro
                    c.setForeground(Color.WHITE); // Muda o texto para branco destacando no fundo
                } // Finaliza os testes da seleção da linha
                return c; // Entrega o componente com as cores adaptadas
            } // Conclui a sobreescrita do renderizador de célula
        }; // Fechamento da instância do TableCellRenderer anônimo
        for (int i = 0; i < cols.length; i++) // Inicia um loop passando por todos os índices de colunas existentes
            table.getColumnModel().getColumn(i).setCellRenderer(tipoRenderer); // Aplica nosso renderizador a cada uma destas colunas

        // Permite ordenação clicando no cabeçalho
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model); // Inicializa o sistema de classificação atrelado ao nosso modelo
        table.setRowSorter(sorter); // Insere essa funcionalidade de classificar na tabela

        JScrollPane sp = new JScrollPane(table); // Embala a tabela construída dentro de um novo JScrollPane para conter rolagem
        sp.setBorder(BorderFactory.createEmptyBorder()); // Retira todas as bordas visíveis em torno dessa rolagem
        sp.getViewport().setBackground(Tema.FUNDO); // Colore a área livre atrás da tabela com a mesma cor padrão de fundo
        return sp; // Retorna este recipiente com barra de rolagem contendo a tabela
    } // Finaliza o método buildTable

    private JPanel buildFooter() { // Função interna para desenhar o pedaço de rodapé
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4)); // Inicia um painel alinhado rigorosamente para direita
        p.setOpaque(false); // Descarta o fundo para o painel se fundir com o fundo da página
        lblTotal = new JLabel("Prejuízo Total (descartes): R$ 0,00"); // Inicializa o rótulo do valor zerado
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Formata o tamanho em 15 com negrito
        lblTotal.setForeground(Tema.CRITICO_TXT); // Aplica o tom de alerta (vermelho) para simbolizar dinheiro perdido
        p.add(lblTotal); // Adiciona essa mensagem ao painel criado
        return p; // Entrega o rodapé construído
    } // Termina o método buildFooter

    // ----------------------------------------------------------------- refresh
    public void refresh() { // Método para carregar novamente e mostrar informações precisas na tabela
        model.setRowCount(0); // Destrói e retira todas as informações já exibidas nas linhas
        String filtro = cbFiltro != null ? (String) cbFiltro.getSelectedItem() : "Todos"; // Detecta a opção eleita no menu suspenso ou fixa 'Todos' se o controle ainda não existir

        // Calcula o prejuízo total de todos os descartes (independente do filtro)
        double totalPrejuizo = EstoqueStore.get().getDescartes().stream() // Cria um fluxo enumerando cada descarte feito no sistema
                .mapToDouble(Descarte::calcularPrejuizo).sum(); // Converte essas informações unicamente em reais perdidos e os soma integralmente

        for (Movimentacao mov : EstoqueStore.get().getMovimentacoes()) { // Inicia um loop sobre todo o histórico de fluxos (movimentações)
            String tipoStr = mov.getTipo().getDescricao(); // Obtém a categoria em que se encaixa ("Baixa", "Inclusão", "Descarte")

            // Aplica filtro de tipo
            if (!"Todos".equals(filtro) && !filtro.equals(tipoStr)) continue; // Interrompe o andamento desse item específico no loop caso o tipo não iguale o que foi pedido no filtro

            // Calcula prejuízo apenas para descartes
            String prejStr = "—"; // Inicia a variável do texto de prejuízo preenchida por um travessão
            if (mov.getTipo() == TipoMovimentacao.DESCARTE) { // Restringe o cálculo seguinte somente aos cenários em que o produto foi de fato descartado
                // Busca o descarte correspondente para calcular o prejuízo real
                double prej = EstoqueStore.get().getDescartes().stream() // Aciona outro fluxo lendo apenas a relação de itens descartados
                        .filter(d -> d.getLote() != null && mov.getLote() != null // Passa pelo filtro atestando a presença de lote nas duas partes
                                && d.getLote().getIdLote() == mov.getLote().getIdLote() // Assegura com certeza tratar-se do mesmo registro de lote
                                && d.getDataDescarte().equals(mov.getData()) // Examina se a data bate no movimento e no descarte
                                && d.getQuantidadeDescartada() == mov.getQuantidade()) // Confirma a veracidade medindo a exata quantidade do produto
                        .mapToDouble(Descarte::calcularPrejuizo) // Seleciona apenas a quantia monetária
                        .findFirst() // Encerra a checagem no primeiro que passar por todas as provações
                        .orElse(0.0); // Concede o zero se nada sobreviveu no filtro anterior
                if (prej > 0) { // Comprova se existe qualquer valor maior que zero em prejuízo
                    prejStr = String.format("R$ %.2f", prej).replace('.', ','); // Modela a moeda separada por vírgula em sua variável de texto
                } // Fim da atribuição do prejuízo financeiro
            } // Encerra a verificação para os descartes

            model.addRow(new Object[]{ // Povoa e desenha uma única linha, composta pelos oito parâmetros, no visual
                mov.getId(), // Aloca o identificador numeral da movimentação
                tipoStr, // Preenche a categoria classificada ("Descarte", etc.)
                mov.getNomeProduto() != null ? mov.getNomeProduto().toUpperCase() : "—", // Posiciona o título todo maiúsculo ou a linha de falta
                mov.getNumeroLote(), // Expõe o indicativo ou código do lote
                mov.getQuantidade(), // Anuncia quantas unidades sofreram o efeito
                mov.getData() != null ? mov.getData().format(FMT) : "—", // Põe a data modificada para o padrão do dia-a-dia
                mov.getObservacao() != null ? mov.getObservacao() : "—", // Repassa o motivo ou observação caso haja
                prejStr // Entrega a conversão escrita do prejuízo ao último espaço
            }); // Finaliza o cadastro deste lote na estrutura da tabela
        } // Encerra as voltas por todos os acontecimentos salvos

        if (lblTotal != null) { // Confirma que o rótulo do total da página esteja válido antes de escrever
            lblTotal.setText("Prejuízo Total (descartes): " // Substitui todo texto por "Prejuízo Total (descartes): "
                    + String.format("R$ %.2f", totalPrejuizo).replace('.', ',')); // E concilia este texto com a fatia montante previamente convertida
        } // Fim das ações sobre o total
    } // Fim do método para atualizar a janela
} // Fim da codificação de DescartesPanel