package com.mycompany.contole_estoque.export;

import com.mycompany.contole_estoque.*;
import com.mycompany.contole_estoque.store.EstoqueStore;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Esta classe é responsável por criar a planilha Excel (.xlsx) com todos os dados do sistema.
 * Ela organiza as informações em diferentes "abas" (como as divisórias de uma pasta).
 */
public class ExportadorExcel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Método que gera o arquivo Excel no caminho escolhido pelo usuário.
     * @param caminhoArquivo
     * @throws java.io.IOException
     */
    public static void exportar(String caminhoArquivo) throws IOException {
        // Cria um novo arquivo de Excel em branco
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // Cria um estilo para o cabeçalho (negrito, texto branco e fundo azul acinzentado)
            CellStyle estiloCabecalho = criarEstiloCabecalho(workbook);

            // Cria cada uma das abas da planilha
            criarAbaPereciveis(workbook, estiloCabecalho);
            criarAbaNaoPereciveis(workbook, estiloCabecalho);
            criarAbaLotes(workbook, estiloCabecalho);
            criarAbaDescartes(workbook, estiloCabecalho);
            criarAbaMovimentacoes(workbook, estiloCabecalho);

            // Salva o arquivo no disco (computador)
            try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
                workbook.write(fos);
            }
        }
    }

    // --- Cria a aba de Produtos Perecíveis ---
    private static void criarAbaPereciveis(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Perecíveis");
        String[] colunas = {"ID", "Nome", "Categoria", "Preço Unitário", "Estoque Mínimo"};
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (ProdutoPerecivel p : EstoqueStore.get().getPerec()) {
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getNome());
            row.createCell(2).setCellValue(p.getCategoria());
            row.createCell(3).setCellValue(p.getPrecoUnitario());
            row.createCell(4).setCellValue(p.getEstoqueMinimo());
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    // --- Cria a aba de Produtos Não Perecíveis ---
    private static void criarAbaNaoPereciveis(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Não Perecíveis");
        String[] colunas = {"ID", "Nome", "Categoria", "Preço Unitário", "Estoque Mínimo"};
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (ProdutoNaoPerecivel p : EstoqueStore.get().getNaoPerec()) {
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getNome());
            row.createCell(2).setCellValue(p.getCategoria());
            row.createCell(3).setCellValue(p.getPrecoUnitario());
            row.createCell(4).setCellValue(p.getEstoqueMinimo());
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    // --- Cria a aba do Estoque Atual (Lotes) ---
    private static void criarAbaLotes(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Estoque (Lotes)");
        String[] colunas = {"Lote", "Produto", "Tipo", "Quantidade", "Data de Entrada", "Data de Validade", "Status"};
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (LoteEstoque lote : EstoqueStore.get().getLotes()) {
            Produto produto = lote.getProduto();
            String nomeProduto = (produto != null) ? produto.getNome() : "(removido)";
            String tipo = (produto instanceof ProdutoPerecivel) ? "Perecível" : "Não Perecível";
            
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(lote.getNumeroLote());
            row.createCell(1).setCellValue(nomeProduto);
            row.createCell(2).setCellValue(tipo);
            row.createCell(3).setCellValue(lote.getQuantidade());
            row.createCell(4).setCellValue(lote.getDataEntrada().format(FMT));
            row.createCell(5).setCellValue(lote.getDataValidade() != null ? lote.getDataValidade().format(FMT) : "—");
            row.createCell(6).setCellValue(lote.isVencido() ? "Vencido" : "OK");
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    // --- Cria a aba de Histórico de Movimentações ---
    private static void criarAbaMovimentacoes(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Histórico de Movimentações");
        String[] colunas = {"ID", "Tipo", "Produto", "Lote", "Quantidade", "Data", "Observação"};
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (Movimentacao mov : EstoqueStore.get().getMovimentacoes()) {
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(mov.getId());
            row.createCell(1).setCellValue(mov.getTipo().getDescricao());
            row.createCell(2).setCellValue(mov.getNomeProduto());
            row.createCell(3).setCellValue(mov.getNumeroLote());
            row.createCell(4).setCellValue(mov.getQuantidade());
            row.createCell(5).setCellValue(mov.getData().format(FMT));
            row.createCell(6).setCellValue(mov.getObservacao());
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    // --- Aba extra de Descartes (Perdas) ---
    private static void criarAbaDescartes(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Descartes");
        String[] colunas = {"ID", "Produto", "Lote", "Quantidade", "Data", "Motivo", "Prejuízo (R$)"};
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (Descarte d : EstoqueStore.get().getDescartes()) {
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(d.getDescarteId());
            row.createCell(1).setCellValue(d.getLote().getProduto() != null ? d.getLote().getProduto().getNome() : "—");
            row.createCell(2).setCellValue(d.getLote().getNumeroLote());
            row.createCell(3).setCellValue(d.getQuantidadeDescartada());
            row.createCell(4).setCellValue(d.getDataDescarte().format(FMT));
            row.createCell(5).setCellValue(d.getMotivo());
            row.createCell(6).setCellValue(d.calcularPrejuizo());
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    // Helpers para formatar a planilha
    private static CellStyle criarEstiloCabecalho(XSSFWorkbook wb) {
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static void escreverCabecalho(Sheet sheet, String[] colunas, CellStyle estilo) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < colunas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(colunas[i]);
            cell.setCellStyle(estilo);
        }
        sheet.createFreezePane(0, 1); // Trava a primeira linha no topo
    }

    private static void autoAjustarColunas(Sheet sheet, int numColunas) {
        for (int i = 0; i < numColunas; i++) sheet.autoSizeColumn(i);
    }
}
