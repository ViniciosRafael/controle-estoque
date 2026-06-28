package com.mycompany.contole_estoque.gui;

import com.mycompany.contole_estoque.config.ConfiguracoesStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import static java.awt.Component.LEFT_ALIGNMENT;

/**
 * Painel de Configurações do sistema.
 *
 * Permite habilitar ou desabilitar globalmente os tipos de produto
 * (Perecível / Não Perecível) aceitos para novo cadastro. Ao desabilitar um
 * tipo aqui, a opção correspondente deixa de aparecer no diálogo "Novo
 * Produto" — produtos desse tipo já cadastrados não são afetados.
 */
public class ConfiguracoesPanel extends JPanel {

    private static final Color BG          = Color.WHITE;
    private static final Color CARD_BG     = new Color(248, 249, 252);
    private static final Color BORDER_CLR  = new Color(220, 222, 232);
    private static final Color TEXT_TITLE  = new Color(20, 22, 35);
    private static final Color TEXT_SUB    = new Color(120, 125, 145);

    private JCheckBox chkPereciveis;
    private JCheckBox chkNaoPereciveis;

    public ConfiguracoesPanel() {
        setBackground(BG);
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(28, 28, 28, 28));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        refresh();
    }

    // ------------------------------------------------------------------ build
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Configurações");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_TITLE);
        p.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("Preferências gerais do sistema");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_SUB);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(buildCardTiposProduto());
        return center;
    }

    private JPanel buildCardTiposProduto() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(560, 220));
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(20, 22, 20, 22)
        ));

        JLabel titulo = new JLabel("Tipos de Produto Aceitos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(TEXT_TITLE);
        titulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel explicacao = new JLabel(
                "<html>Defina quais tipos de produto podem ser cadastrados no sistema.<br>"
              + "Produtos já cadastrados de um tipo desabilitado não são afetados.</html>");
        explicacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        explicacao.setForeground(TEXT_SUB);
        explicacao.setAlignmentX(LEFT_ALIGNMENT);
        explicacao.setBorder(new EmptyBorder(4, 0, 14, 0));

        chkPereciveis = criarCheckbox("Permitir cadastro de produtos Perecíveis");
        chkNaoPereciveis = criarCheckbox("Permitir cadastro de produtos Não Perecíveis");

        chkPereciveis.addActionListener(e -> {
            boolean aplicado = ConfiguracoesStore.get().setPereciveisHabilitados(chkPereciveis.isSelected());
            if (!aplicado) {
                avisarAoMenosUmTipo();
                chkPereciveis.setSelected(true); // desfaz a mudança na tela
            }
        });

        chkNaoPereciveis.addActionListener(e -> {
            boolean aplicado = ConfiguracoesStore.get().setNaoPereciveisHabilitados(chkNaoPereciveis.isSelected());
            if (!aplicado) {
                avisarAoMenosUmTipo();
                chkNaoPereciveis.setSelected(true); // desfaz a mudança na tela
            }
        });

        card.add(titulo);
        card.add(explicacao);
        card.add(chkPereciveis);
        card.add(Box.createVerticalStrut(8));
        card.add(chkNaoPereciveis);

        return card;
    }

    private JCheckBox criarCheckbox(String texto) {
        JCheckBox chk = new JCheckBox(texto);
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chk.setForeground(TEXT_TITLE);
        chk.setOpaque(false);
        chk.setAlignmentX(LEFT_ALIGNMENT);
        chk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chk.setFocusPainted(false);
        return chk;
    }

    private void avisarAoMenosUmTipo() {
        JOptionPane.showMessageDialog(this,
                "Pelo menos um tipo de produto precisa permanecer habilitado.",
                "Ação não permitida",
                JOptionPane.WARNING_MESSAGE);
    }

    // ----------------------------------------------------------------- refresh
    /** Sincroniza os checkboxes com o estado atual salvo em ConfiguracoesStore. */
    public void refresh() {
        chkPereciveis.setSelected(ConfiguracoesStore.get().isPereciveisHabilitados());
        chkNaoPereciveis.setSelected(ConfiguracoesStore.get().isNaoPereciveisHabilitados());
    }
}