package com.mycompany.contole_estoque.export;

import com.mycompany.contole_estoque.Descarte;
import com.mycompany.contole_estoque.LoteEstoque;
import com.mycompany.contole_estoque.Produto;
import com.mycompany.contole_estoque.ProdutoNaoPerecivel;
import com.mycompany.contole_estoque.ProdutoPerecivel;
import com.mycompany.contole_estoque.store.EstoqueStore;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Exporta todos os dados do {@link EstoqueStore} (produtos perecíveis,
 * não perecíveis, lotes de estoque e descartes) para um único arquivo
 * Excel (.xlsx), com uma aba (planilha) para cada tipo de dado.
 *
 * Usa a biblioteca Apache POI (poi-ooxml) para gerar o arquivo.
 */
public class ExportadorExcel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Gera o arquivo Excel no caminho informado, com os dados atuais do
     * EstoqueStore.
     *
     * @param caminhoArquivo caminho completo do arquivo .xlsx a ser criado
     *                       (ex.: "C:\\Users\\nome\\Desktop\\estoque.xlsx")
     * @throws IOException se houver erro ao escrever o arquivo no disco
     */
    public static void exportar(String caminhoArquivo) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            CellStyle estiloCabecalho = criarEstiloCabecalho(workbook);

            criarAbaPereciveis(workbook, estiloCabecalho);
            criarAbaNaoPereciveis(workbook, estiloCabecalho);
            criarAbaLotes(workbook, estiloCabecalho);
            criarAbaDescartes(workbook, estiloCabecalho);

            try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
                workbook.write(fos);
            }
        }
    }

    // ------------------------------------------------------------------ abas
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

    private static void criarAbaLotes(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Estoque (Lotes)");
        String[] colunas = {
                "Lote", "Produto", "Tipo", "Quantidade",
                "Data de Entrada", "Data de Validade", "Status"
        };
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (LoteEstoque lote : EstoqueStore.get().getLotes()) {
            Produto produto = lote.getProduto();

            // Lote "órfão" (produto removido depois de já ter estoque
            // cadastrado): exporta a linha mesmo assim, com um texto
            // substituto, em vez de lançar NullPointerException e
            // interromper a exportação no meio do arquivo.
            String nomeProduto = (produto != null) ? produto.getNome() : "(produto removido)";
            String tipo = (produto instanceof ProdutoPerecivel) ? "Perecível"
                        : (produto != null) ? "Não Perecível" : "—";

            String status;
            String validade = "—";
            if (lote.getDataValidade() != null) {
                validade = lote.getDataValidade().format(FMT);
                if (lote.isVencido()) {
                    status = "Vencido";
                } else if (lote.diasParaVencer() <= 5) {
                    status = "Próx. Vencimento (" + lote.diasParaVencer() + "d)";
                } else {
                    status = "OK";
                }
            } else {
                status = "OK (sem vencimento)";
            }

            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(lote.getNumeroLote());
            row.createCell(1).setCellValue(nomeProduto);
            row.createCell(2).setCellValue(tipo);
            row.createCell(3).setCellValue(lote.getQuantidade());
            row.createCell(4).setCellValue(lote.getDataEntrada() != null ? lote.getDataEntrada().format(FMT) : "—");
            row.createCell(5).setCellValue(validade);
            row.createCell(6).setCellValue(status);
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    private static void criarAbaDescartes(XSSFWorkbook wb, CellStyle estiloCabecalho) {
        Sheet sheet = wb.createSheet("Descartes");
        String[] colunas = {
                "ID", "Produto", "Quantidade Descartada",
                "Data do Descarte", "Motivo", "Prejuízo (R$)"
        };
        escreverCabecalho(sheet, colunas, estiloCabecalho);

        int linha = 1;
        for (Descarte d : EstoqueStore.get().getDescartes()) {
            Produto produto = d.getLote().getProduto();
            String nomeProduto = (produto != null) ? produto.getNome() : "(produto removido)";

            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(d.getDescarteId());
            row.createCell(1).setCellValue(nomeProduto);
            row.createCell(2).setCellValue(d.getQuantidadeDescartada());
            row.createCell(3).setCellValue(d.getDataDescarte() != null ? d.getDataDescarte().format(FMT) : "—");
            row.createCell(4).setCellValue(d.getMotivo());
            row.createCell(5).setCellValue(d.calcularPrejuizo());
        }
        autoAjustarColunas(sheet, colunas.length);
    }

    // ----------------------------------------------------------------- estilo
    private static CellStyle criarEstiloCabecalho(XSSFWorkbook wb) {
        Font fonteNegrito = wb.createFont();
        fonteNegrito.setBold(true);
        fonteNegrito.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());

        CellStyle estilo = wb.createCellStyle();
        estilo.setFont(fonteNegrito);
        estilo.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.BLUE_GREY.getIndex());
        estilo.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        return estilo;
    }

    private static void escreverCabecalho(Sheet sheet, String[] colunas, CellStyle estilo) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < colunas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(colunas[i]);
            cell.setCellStyle(estilo);
        }
        // Congela a primeira linha (cabeçalho) ao rolar a planilha
        sheet.createFreezePane(0, 1);
    }

    /**
     * Ajusta a largura das colunas ao conteúdo. Para planilhas muito grandes
     * (ex.: 50.000+ linhas), autoSizeColumn pode ser lento; por isso
     * limitamos a operação a no máximo as colunas informadas, e o método
     * já é eficiente o bastante para os tamanhos de dataset deste sistema.
     */
    private static void autoAjustarColunas(Sheet sheet, int numColunas) {
        for (int i = 0; i < numColunas; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}