package com.davixavier.utils.pdf;

import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.estoque.Produto;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

public class ProdutoVendaPDFTRow extends PDFRow<ProdutoVenda>
{
	private CustomPDFTCell nomeCell;
	private CustomPDFTCell idCell;
	private CustomPDFTCell pre�oCell;
	private CustomPDFTCell quantidadeCell;
	private CustomPDFTCell totalCell;
	
	public ProdutoVendaPDFTRow(ProdutoVenda produto) 
	{
		nomeCell = new CustomPDFTCell(produto.getNome());
		idCell = new CustomPDFTCell(produto.getId() + "");
		pre�oCell = new CustomPDFTCell(String.format("%.2f", produto.getPre�o()).replace(".", ","));
		totalCell = new CustomPDFTCell(String.format("%.2f", produto.getPre�o() * produto.getQuantidade()).replace(".", ","));
		quantidadeCell = new CustomPDFTCell(produto.getQuantidade() + "");
		
		idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nomeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pre�oCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		nomeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		idCell.setVerticalAlignment(Element.ALIGN_CENTER);
		pre�oCell.setVerticalAlignment(Element.ALIGN_CENTER);
		totalCell.setVerticalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setVerticalAlignment(Element.ALIGN_CENTER);
	}

	public CustomPDFTCell getNomeCell() {
		return nomeCell;
	}

	public void setNomeCell(CustomPDFTCell nomeCell) {
		this.nomeCell = nomeCell;
	}

	public CustomPDFTCell getIdCell() {
		return idCell;
	}

	public void setIdCell(CustomPDFTCell idCell) {
		this.idCell = idCell;
	}

	public CustomPDFTCell getPre�oCell() {
		return pre�oCell;
	}

	public void setPre�oCell(CustomPDFTCell pre�oCell) {
		this.pre�oCell = pre�oCell;
	}

	public CustomPDFTCell getQuantidadeCell() {
		return quantidadeCell;
	}

	public void setQuantidadeCell(CustomPDFTCell quantidadeCell) {
		this.quantidadeCell = quantidadeCell;
	}

	public CustomPDFTCell getTotalCell() {
		return totalCell;
	}

	public void setTotalCell(CustomPDFTCell totalCell) {
		this.totalCell = totalCell;
	}

}
