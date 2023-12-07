package com.davixavier.utils.pdf;

import com.davixavier.entidades.estoque.Produto;
import com.itextpdf.text.Element;

public class ProdutoPDFTRow extends PDFRow<Produto>
{
	private CustomPDFTCell codigoCell;
	private CustomPDFTCell nomeCell;
	private CustomPDFTCell quantidadeCell;
	private CustomPDFTCell pre�oCell;
	private CustomPDFTCell pre�oCompraCell;
	private CustomPDFTCell lucroCell;
	
	public ProdutoPDFTRow(Produto produto) 
	{
		codigoCell = new CustomPDFTCell(produto.getCodigo() + "");
		nomeCell = new CustomPDFTCell(produto.getNome());
		quantidadeCell = new CustomPDFTCell(produto.getQuantidade() + "");
		pre�oCell = new CustomPDFTCell(String.format("%.2f", produto.getPre�o()));
		pre�oCompraCell = new CustomPDFTCell(String.format("%.2f", produto.getPre�oCompra()));
		lucroCell = new CustomPDFTCell(String.format("%.2f", produto.getPre�o() - produto.getPre�oCompra()));
		
		codigoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nomeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pre�oCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pre�oCompraCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		lucroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		nomeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		codigoCell.setVerticalAlignment(Element.ALIGN_CENTER);
		pre�oCell.setVerticalAlignment(Element.ALIGN_CENTER);
		pre�oCompraCell.setVerticalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		lucroCell.setVerticalAlignment(Element.ALIGN_CENTER);
	}

	public CustomPDFTCell getCodigoCell() {
		return codigoCell;
	}

	public CustomPDFTCell getNomeCell() {
		return nomeCell;
	}

	public CustomPDFTCell getQuantidadeCell() {
		return quantidadeCell;
	}

	public CustomPDFTCell getPre�oCell() {
		return pre�oCell;
	}

	public CustomPDFTCell getPre�oCompraCell() {
		return pre�oCompraCell;
	}

	public CustomPDFTCell getLucroCell() {
		return lucroCell;
	}
}
