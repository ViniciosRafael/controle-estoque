package com.mycompany.contole_estoque.gui; // Define o pacote desta classe

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

// Painel de Alertas — lista alertas ativos com filtro por tipo.
// Botão para regerar alertas com base no estado atual dos lotes.
public class AlertasPanel extends JPanel { // Define a classe do painel de alertas estendendo JPanel

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Define o formato de data padrão

    private DefaultTableModel model; // Declara o modelo de dados da tabela
    private JComboBox<String> cbFiltro; // Declara a caixa de seleção para filtro

    public AlertasPanel() { // Construtor da classe
        setLayout(new BorderLayout(0, 16)); // Configura o layout da tela como BorderLayout
        setBorder(new EmptyBorder(28, 28, 28, 28)); // Adiciona uma margem interna ao painel
        add(buildHeader(), BorderLayout.NORTH); // Adiciona o cabeçalho no topo
        add(buildTable(),  BorderLayout.CENTER); // Adiciona a tabela no centro
        refresh(); // Atualiza os dados da tela
    } // Fim do construtor

    private JPanel buildHeader() { // Método para construir o cabeçalho
        JPanel p = new JPanel(new BorderLayout()); // Cria o painel do cabeçalho
        p.setOpaque(false); // Define o painel como transparente

        JLabel title = new JLabel("Alertas do Sistema"); // Cria o título da tela
        title.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Define a fonte do título
        p.add(title, BorderLayout.WEST); // Adiciona o título à esquerda

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Cria painel de controles à direita
        ctrl.setOpaque(false); // Define o painel de controles como transparente

        JLabel lblFiltro = new JLabel("Filtrar:"); // Cria o rótulo do filtro
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Define a fonte do rótulo
        cbFiltro = new JComboBox<>(new String[]{"Todos","VENCIMENTO","ESTOQUE_MINIMO"}); // Inicializa o combo box de filtros
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Define a fonte do combo box
        cbFiltro.addActionListener(e -> refresh()); // Atualiza os dados ao selecionar um filtro

        JButton btnGerar = ProdutosPanel.actionButton("Gerar Alertas", new Color(0, 120, 210)); // Cria botão para gerar alertas
        btnGerar.addActionListener(e -> { // Adiciona ação ao botão de gerar alertas
            EstoqueStore.get().gerarAlertas(); // Chama a geração de alertas na loja
            refresh(); // Atualiza a tabela de alertas
            JOptionPane.showMessageDialog(this, // Mostra um diálogo de mensagem
                EstoqueStore.get().getAlertas().size() + " alerta(s) gerado(s).", // Mensagem com a quantidade de alertas
                "Alertas Atualizados", JOptionPane.INFORMATION_MESSAGE); // Título e tipo do diálogo
        }); // Fim da ação do botão

        ctrl.add(lblFiltro); // Adiciona o rótulo ao painel de controles
        ctrl.add(cbFiltro); // Adiciona o combo box ao painel de controles
        ctrl.add(btnGerar); // Adiciona o botão de gerar alertas ao painel
        p.add(ctrl, BorderLayout.EAST); // Adiciona controles no lado direito do cabeçalho
        return p; // Retorna o cabeçalho construído
    } // Fim do método buildHeader

    private JScrollPane buildTable() { // Método para construir a tabela
        String[] cols = {"ID","Tipo","Mensagem","Data"}; // Define as colunas da tabela
        model = new DefaultTableModel(cols, 0) { // Inicializa o modelo da tabela
            @Override public boolean isCellEditable(int r, int c) { return false; } // Impede a edição das células
        }; // Fim da definição do modelo

        JTable table = new JTable(model); // Cria a tabela com o modelo
        DashboardPanel.styleTable(table); // Aplica estilo na tabela
        table.getColumnModel().getColumn(0).setPreferredWidth(45); // Define largura da coluna ID
        table.getColumnModel().getColumn(1).setPreferredWidth(160); // Define largura da coluna Tipo
        table.getColumnModel().getColumn(2).setPreferredWidth(560); // Define largura da coluna Mensagem
        table.getColumnModel().getColumn(3).setPreferredWidth(110); // Define largura da coluna Data

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() { // Define um renderizador para a tabela
            @Override public Component getTableCellRendererComponent( // Sobrescreve o método de renderização
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) { // Parâmetros do renderizador
                Component comp = super.getTableCellRendererComponent(t, v, sel, focus, r, c); // Obtém o componente padrão
                if (!sel) { // Verifica se a linha não está selecionada
                    String tipo = (String) model.getValueAt(r, 1); // Obtém o tipo de alerta da linha
                    comp.setForeground("VENCIMENTO".equals(tipo) // Define a cor do texto com base no tipo
                        ? new Color(255, 170, 0) : new Color(255, 90, 90)); // Laranja para vencimento, vermelho para mínimo
                } // Fim do bloco if
                return comp; // Retorna o componente configurado
            } // Fim do método de renderização
        }); // Fim da definição do renderizador

        JScrollPane sp = new JScrollPane(table); // Envolve a tabela num painel de rolagem
        sp.setBorder(BorderFactory.createEmptyBorder()); // Remove a borda do painel de rolagem
        return sp; // Retorna a tabela pronta
    } // Fim do método buildTable

    // ----------------------------------------------------------------- refresh
    private void refresh() { // Método para atualizar os dados
        model.setRowCount(0); // Limpa as linhas da tabela
        String filtro = (String) cbFiltro.getSelectedItem(); // Obtém o filtro selecionado
        for (Alerta a : EstoqueStore.get().getAlertas()) { // Itera sobre todos os alertas
            if ("Todos".equals(filtro) || a.getTipo().equals(filtro)) { // Verifica se atende ao filtro
                model.addRow(new Object[]{ // Adiciona uma nova linha
                    a.getAlertaId(), a.getTipo(), a.getMensagem(), a.getData().format(FMT) // Passa os dados do alerta
                }); // Fim da adição da linha
            } // Fim do bloco if
        } // Fim do loop
    } // Fim do método refresh
} // Fim da classe AlertasPanel