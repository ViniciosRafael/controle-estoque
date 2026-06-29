package com.mycompany.contole_estoque.gui.theme;

import java.awt.Color;

/**
 * Paleta de cores da identidade visual FlowControl, centralizada para uso
 * em todas as telas do sistema.
 *
 * Atualizado para TEMA CLARO conforme solicitado.
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

    // ----------------------------------------------------- variantes escuras para texto
    // (para uso como TEXTO sobre fundo claro)

    /** Verde-teal escuro — versão da PRIMARIA legível como texto sobre fundo claro. */
    public static final Color PRIMARIA_TXT = new Color(0x0F, 0x6E, 0x56);

    /** Amber escuro — versão da ALERTA legível como texto sobre fundo claro. */
    public static final Color ALERTA_TXT = new Color(0x8A, 0x5A, 0x00);

    /** Vermelho escuro — versão da CRITICO legível como texto sobre fundo claro. */
    public static final Color CRITICO_TXT = new Color(0xA8, 0x2C, 0x2C);

    // --------------------------------------------------- superfícies claras (ATUALIZADO)

    /** Fundo principal de janela/conteúdo - Branco. */
    public static final Color FUNDO = new Color(0xFF, 0xFF, 0xFF);

    /** Fundo de cards, tabelas e painéis elevados - Cinza muito claro. */
    public static final Color CARD_BG = new Color(0xF8, 0xF9, 0xFC);

    /** Fundo do cabeçalho de tabelas. */
    public static final Color HEADER_BG = new Color(0xF1, 0xF3, 0xF9);

    /** Fundo de campos de formulário (inputs). */
    public static final Color CAMPO_BG = new Color(0xFF, 0xFF, 0xFF);

    /** Borda padrão entre elementos. */
    public static final Color BORDA = new Color(0xDC, 0xDE, 0xE6);

    // ------------------------------------------------------- texto sobre claro

    /** Texto principal (títulos, valores) sobre fundo claro — Preto suave. */
    public static final Color TEXTO_TITULO = new Color(0x14, 0x16, 0x23);

    /** Texto secundário (subtítulos, legendas) sobre fundo claro — Cinza médio. */
    public static final Color TEXTO_SUB = new Color(0x46, 0x4B, 0x5F);

    /** Texto de cabeçalho de tabela sobre fundo claro. */
    public static final Color TEXTO_HEADER = new Color(0x46, 0x4B, 0x5F);
}
