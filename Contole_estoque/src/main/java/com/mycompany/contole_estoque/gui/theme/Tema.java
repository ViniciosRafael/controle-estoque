package com.mycompany.contole_estoque.gui.theme;

import java.awt.Color;

/**
 * Paleta de cores da identidade visual FlowControl, centralizada para uso
 * em todas as telas do sistema.
 *
 * Tema escuro: fundos de janela, cards e tabelas usam tons de cinza-chumbo
 * (em vez do branco/quase-branco usado anteriormente), e os textos usam
 * tons claros para manter contraste e legibilidade.
 *
 * Significado de cada cor (conforme guia de identidade visual FlowControl):
 *  - PRIMARIA: cor de marca, usada em botões e elementos principais de ação.
 *  - ACENTO:   tom mais escuro da primária, usado em hover/estados ativos.
 *  - DESTAQUE: verde-teal claro, usado em realces suaves e fundos de chips.
 *  - ALERTA:   amber, para avisos de estoque baixo / próximo do vencimento.
 *  - CRITICO:  vermelho, para itens vencidos ou situações críticas.
 *
 * As variantes "_TXT" são versões mais claras das cores de marca, usadas
 * quando a cor aparece como TEXTO sobre fundo escuro (em vez de como fundo
 * de botão) — tons saturados e escuros perdem contraste/legibilidade
 * quando usados como texto sobre um fundo já escuro.
 */
public final class Tema {

    private Tema() {} // classe utilitária, não deve ser instanciada

    /** Verde-teal primário da marca (#1D9E75). Usado como fundo de botões e elementos de ação. */
    public static final Color PRIMARIA = new Color(0x1D, 0x9E, 0x75);

    /** Verde-teal escuro de acento (#0F6E56). Usado em hover/estados ativos sobre a cor primária. */
    public static final Color ACENTO = new Color(0x0F, 0x6E, 0x56);

    /** Verde-teal claro de destaque (#9FE1CB). Usado em realces suaves, badges e fundos leves. */
    public static final Color DESTAQUE = new Color(0x9F, 0xE1, 0xCB);

    /** Amber de alerta (#EF9F27). Usado para estoque mínimo, vencimento próximo, etc. */
    public static final Color ALERTA = new Color(0xEF, 0x9F, 0x27);

    /** Vermelho crítico (#E24B4A). Usado para itens vencidos, exclusões e erros. */
    public static final Color CRITICO = new Color(0xE2, 0x4B, 0x4A);

    /** Neutro escuro (#2C2C2A). */
    public static final Color NEUTRO = new Color(0x2C, 0x2C, 0x2A);

    // ----------------------------------------------------- variantes claras
    // (para uso como TEXTO sobre fundo escuro, em vez de fundo de botão)

    /** Verde-teal claro (#5DCAA5) — versão da PRIMARIA legível como texto sobre fundo escuro. */
    public static final Color PRIMARIA_TXT = new Color(0x5D, 0xCA, 0xA5);

    /** Amber claro (#FAC775) — versão da ALERTA legível como texto sobre fundo escuro. */
    public static final Color ALERTA_TXT = new Color(0xFA, 0xC7, 0x75);

    /** Vermelho claro (#F09595) — versão da CRITICO legível como texto sobre fundo escuro. */
    public static final Color CRITICO_TXT = new Color(0xF0, 0x95, 0x95);

    // --------------------------------------------------- superfícies escuras

    /** Fundo principal de janela/conteúdo (substitui o branco). */
    public static final Color FUNDO = new Color(0x1E, 0x1F, 0x24);

    /** Fundo de cards, tabelas e painéis elevados (um tom mais claro que FUNDO). */
    public static final Color CARD_BG = new Color(0x2A, 0x2B, 0x32);

    /** Fundo do cabeçalho de tabelas (um tom mais claro que CARD_BG). */
    public static final Color HEADER_BG = new Color(0x2F, 0x30, 0x37);

    /** Fundo de campos de formulário (inputs). */
    public static final Color CAMPO_BG = new Color(0x26, 0x27, 0x2D);

    /** Borda padrão entre elementos sobre fundo escuro. */
    public static final Color BORDA = new Color(0x3C, 0x3D, 0x45);

    // ------------------------------------------------------- texto sobre escuro

    /** Texto principal (títulos, valores) sobre fundo escuro — quase branco. */
    public static final Color TEXTO_TITULO = new Color(0xF0, 0xF1, 0xF4);

    /** Texto secundário (subtítulos, legendas) sobre fundo escuro — cinza claro. */
    public static final Color TEXTO_SUB = new Color(0x9A, 0x9C, 0xA6);

    /** Texto de cabeçalho de tabela sobre fundo escuro. */
    public static final Color TEXTO_HEADER = new Color(0xB7, 0xB9, 0xC2);
}