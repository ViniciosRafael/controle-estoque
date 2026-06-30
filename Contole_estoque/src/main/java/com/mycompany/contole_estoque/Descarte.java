package com.mycompany.contole_estoque;
import java.time.LocalDate;

public class Descarte {
    private int descarteId;                    // ID único deste descarte
    private LoteEstoque lote;                  // Lote que foi descartado
    private int quantidadeDescartada;          // Quantidade de unidades perdidas
    private LocalDate dataDescarte;            // Data do descarte
    private String motivo;                     // Motivo do descarte (ex: "Prazo de validade expirado")

    /** Construtor vazio - usado para desserialização ou criação post-hoc */
    public Descarte() {}

    /**
     * Construtor completo do Descarte.
     *
     * @param descarteId            : ID único deste registro
     * @param lote                  : LoteEstoque que foi descartado
     * @param quantidadeDescartada  : Quantidade de unidades perdidas
     * @param dataDescarte          : Data do descarte
     * @param motivo                : Motivo/razão do descarte
     */
    public Descarte(int descarteId, LoteEstoque lote, int quantidadeDescartada, LocalDate dataDescarte, String motivo) {
        this.descarteId = descarteId;
        this.lote = lote;
        this.quantidadeDescartada = quantidadeDescartada;
        this.dataDescarte = dataDescarte;
        this.motivo = motivo;
    }
    public double calcularPrejuizo() {
        Produto produto = this.lote.getProduto();
        if (produto == null) return 0.0; // Lote órfão: sem produto não há como calcular
        double prejuizoUnitario = produto.calcularPrejuizo(); // Preço unitário
        return prejuizoUnitario * this.quantidadeDescartada;  // Total
    }
    public void registrar() {
        // Aplica a baixa no lote
        this.lote.darBaixa(this.quantidadeDescartada);

        // Exibe relatório de auditoria no console
        Produto produto = this.lote.getProduto();
        System.out.println("REGISTRO DE DESCARTE");
        System.out.println("Motivo: " + this.motivo);
        System.out.println("Produto: " + (produto != null ? produto.getNome() : "(produto removido)"));
        System.out.println("Quantidade: " + this.quantidadeDescartada);
        System.out.println("Prejuízo Total: " + this.calcularPrejuizo());
    }

    public int getDescarteId(){return descarteId;}
    public LoteEstoque getLote(){ return lote; }
    public int getQuantidadeDescartada() { return quantidadeDescartada; }
    public LocalDate getDataDescarte(){ return dataDescarte; }
    public String getMotivo(){ return motivo;}

    public void setDescarteId(int descarteId){ this.descarteId= descarteId; }
    public void setLote(LoteEstoque lote){ this.lote= lote; }
    public void setQuantidadeDescartada(int quantidadeDescartada){ this.quantidadeDescartada = quantidadeDescartada; }
    public void setDataDescarte(LocalDate dataDescarte){ this.dataDescarte = dataDescarte; }
    public void setMotivo(String motivo){ this.motivo= motivo; }
}