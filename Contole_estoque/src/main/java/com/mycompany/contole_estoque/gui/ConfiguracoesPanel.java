package com.mycompany.contole_estoque.gui; // Define o pacote desta classe

import com.mycompany.contole_estoque.config.ConfiguracoesStore; // Importa a classe ConfiguracoesStore
import com.mycompany.contole_estoque.gui.theme.Tema; // Importa a classe Tema
import javax.swing.*; // Importa todos os componentes da biblioteca Swing
import javax.swing.border.*; // Importa todas as classes de borda do Swing
import java.awt.*; // Importa todas as classes da biblioteca AWT

// Painel de Configurações do sistema. // Documentação inicial da classe
// // Linha em branco da documentação
// Permite habilitar ou desabilitar globalmente os tipos de produto // Explicação de funcionalidade
// (Perecível / Não Perecível) aceitos para novo cadastro. Ao desabilitar um // Detalhamento de opções
// tipo aqui, a opção correspondente deixa de aparecer no diálogo "Novo // Descrição do impacto
// Produto" — produtos desse tipo já cadastrados não são afetados. // Finalização da descrição
public class ConfiguracoesPanel extends JPanel { // Declara a classe pública estendendo JPanel

    private static final Color BG          = Tema.FUNDO; // Define a constante para a cor de fundo do painel
    private static final Color CARD_BG     = Tema.CARD_BG; // Define a constante para a cor de fundo dos cartões
    private static final Color BORDER_CLR  = Tema.BORDA; // Define a constante para a cor das bordas
    private static final Color TEXT_TITLE  = Tema.TEXTO_TITULO; // Define a constante para a cor do texto do título
    private static final Color TEXT_SUB    = Tema.TEXTO_SUB; // Define a constante para a cor do texto secundário

    private JCheckBox chkPereciveis; // Declara o campo de checkbox para produtos perecíveis
    private JCheckBox chkNaoPereciveis; // Declara o campo de checkbox para produtos não perecíveis

    public ConfiguracoesPanel() { // Declaração do construtor principal
        setBackground(BG); // Configura a cor de fundo principal do componente
        setLayout(new BorderLayout(0, 20)); // Aplica o layout de bordas com espaçamento
        setBorder(new EmptyBorder(28, 28, 28, 28)); // Adiciona margens externas ao painel
        add(buildHeader(), BorderLayout.NORTH); // Posiciona o painel de cabeçalho no topo
        add(buildCenter(), BorderLayout.CENTER); // Posiciona o conteúdo central no meio do layout
        refresh(); // Invoca a atualização visual dos controles
    } // Encerra o escopo do construtor

    // ------------------------------------------------------------------ build // Separador da seção de montagem
    private JPanel buildHeader() { // Método privado para criar a área superior
        JPanel p = new JPanel(new BorderLayout()); // Instancia o contêiner do cabeçalho
        p.setOpaque(false); // Remove o fundo sólido do contêiner

        JLabel title = new JLabel("Configurações"); // Instancia o título da tela
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Configura o estilo da fonte do título
        title.setForeground(TEXT_TITLE); // Aplica a cor definida para o título
        p.add(title, BorderLayout.WEST); // Posiciona o texto à esquerda do cabeçalho

        JLabel sub = new JLabel("Preferências gerais do sistema"); // Cria o texto descritivo menor
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Ajusta a tipografia do subtítulo
        sub.setForeground(TEXT_SUB); // Define a cor para o texto descritivo
        p.add(sub, BorderLayout.SOUTH); // Coloca o subtítulo na base do cabeçalho
        return p; // Entrega o painel finalizado
    } // Finaliza a instrução do método

    private JPanel buildCenter() { // Método para montar a parte central da interface
        JPanel center = new JPanel(); // Cria o componente do centro
        center.setOpaque(false); // Deixa o fundo do centro transparente
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS)); // Estrutura os itens de cima para baixo
        center.add(buildCardTiposProduto()); // Insere o bloco de opções de tipo
        return center; // Retorna o contêiner de opções
    } // Encerra o método de montagem central

    private JPanel buildCardTiposProduto() { // Método para criar a caixa de configurações de produto
        JPanel card = new JPanel(); // Instancia a base gráfica do cartão
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS)); // Organiza seus filhos verticalmente
        card.setBackground(CARD_BG); // Define sua cor de preenchimento
        card.setAlignmentX(LEFT_ALIGNMENT); // Orienta todo o bloco à margem esquerda
        card.setMaximumSize(new Dimension(560, 220)); // Restringe a dimensão máxima da caixa
        card.setBorder(new CompoundBorder( // Usa uma borda dupla
                new LineBorder(BORDER_CLR, 1, true), // Desenha a borda externa fina
                new EmptyBorder(20, 22, 20, 22) // Aplica um preenchimento interno para afastar os filhos da borda
        )); // Fecha a declaração do estilo do cartão

        JLabel titulo = new JLabel("Tipos de Produto Aceitos"); // Instancia o título específico do cartão
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Formata o texto como negrito
        titulo.setForeground(TEXT_TITLE); // Ajusta a cor para o padrão de título
        titulo.setAlignmentX(LEFT_ALIGNMENT); // Alinha o texto do título à esquerda

        JLabel explicacao = new JLabel( // Cria um rótulo multilinhas em HTML
                "<html>Defina quais tipos de produto podem ser cadastrados no sistema.<br>" // Inicia o bloco HTML e a primeira linha
              + "Produtos já cadastrados de um tipo desabilitado não são afetados.</html>"); // Finaliza a segunda linha do aviso
        explicacao.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Diminui a fonte do parágrafo
        explicacao.setForeground(TEXT_SUB); // Define cor atenuada
        explicacao.setAlignmentX(LEFT_ALIGNMENT); // Alinha o texto explicativo à esquerda
        explicacao.setBorder(new EmptyBorder(4, 0, 14, 0)); // Coloca uma margem extra abaixo da descrição

        chkPereciveis = criarCheckbox("Permitir cadastro de produtos Perecíveis"); // Utiliza o método auxiliar para criar a opção 1
        chkNaoPereciveis = criarCheckbox("Permitir cadastro de produtos Não Perecíveis"); // Utiliza o método auxiliar para criar a opção 2

        chkPereciveis.addActionListener(e -> { // Define o que acontece quando a primeira caixa é clicada
            boolean aplicado = ConfiguracoesStore.get().setPereciveisHabilitados(chkPereciveis.isSelected()); // Passa a nova configuração ao gerenciador
            if (!aplicado) { // Verifica se a mudança foi bloqueada pelo backend
                avisarAoMenosUmTipo(); // Chama a função que exibe um alerta de bloqueio
                chkPereciveis.setSelected(true); // Retorna a interface ao estado anterior selecionado // Desfaz a mudança na tela
            } // Encerra a checagem
        }); // Finaliza o gatilho de ação

        chkNaoPereciveis.addActionListener(e -> { // Define o comportamento ao interagir com a segunda caixa
            boolean aplicado = ConfiguracoesStore.get().setNaoPereciveisHabilitados(chkNaoPereciveis.isSelected()); // Tenta persistir a mudança no gerenciador de dados
            if (!aplicado) { // Analisa se não foi possível efetuar a alteração
                avisarAoMenosUmTipo(); // Dispara janela de notificação de impossibilidade
                chkNaoPereciveis.setSelected(true); // Cancela a remoção do clique no visual // Desfaz a mudança na tela
            } // Encerra o laço de bloqueio
        }); // Fim do ouvinte da segunda opção

        card.add(titulo); // Coloca o título principal dentro do contêiner
        card.add(explicacao); // Introduz o texto de instrução abaixo
        card.add(chkPereciveis); // Acrescenta o componente visual de perecíveis
        card.add(Box.createVerticalStrut(8)); // Separa as duas caixas de seleção
        card.add(chkNaoPereciveis); // Acrescenta o controle visual de não perecíveis

        return card; // Fornece o contêiner preparado para ser renderizado
    } // Fim da declaração do criador de painel

    private JCheckBox criarCheckbox(String texto) { // Função para padronizar novas instâncias de checkbox
        JCheckBox chk = new JCheckBox(texto); // Constrói o item da interface com seu respectivo rótulo
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Dá o estilo de fonte base ao texto associado
        chk.setForeground(TEXT_TITLE); // Mantém a padronização de cor das letras
        chk.setOpaque(false); // Retira o desenho do painel traseiro do próprio componente
        chk.setAlignmentX(LEFT_ALIGNMENT); // Empurra a caixa em si para o começo esquerdo
        chk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Indica que a caixa é clicável
        chk.setFocusPainted(false); // Torna a transição de clique mais limpa
        return chk; // Conclui e retorna a caixa configurada
    } // Finaliza a lógica utilitária

    private void avisarAoMenosUmTipo() { // Método centralizado para a mensagem de restrição de negócio
        JOptionPane.showMessageDialog(this, // Inicia a caixa de alerta visual prendendo foco nesta janela
                "Pelo menos um tipo de produto precisa permanecer habilitado.", // Exibe a descrição do problema
                "Ação não permitida", // Insere um título mais legível para a janela
                JOptionPane.WARNING_MESSAGE); // Adiciona um ícone em formato de triângulo de aviso
    } // Conclui a declaração do método informacional

    // ----------------------------------------------------------------- refresh // Separa a seção de atualização do estado
    // Sincroniza os checkboxes com o estado atual salvo em ConfiguracoesStore. // Descrição inicial do método
    public void refresh() { // Método para atualizar os marcadores de seleção
        chkPereciveis.setSelected(ConfiguracoesStore.get().isPereciveisHabilitados()); // Consulta o valor atual para perecíveis e aplica ao checkbox
        chkNaoPereciveis.setSelected(ConfiguracoesStore.get().isNaoPereciveisHabilitados()); // Consulta o valor para não perecíveis e atribui ao componente
    } // Encerra a função pública de sincronismo
} // Fecha o escopo da classe ConfiguracoesPanel