package com.davixavier.utils.pdf;

import com.davixavier.entidades.estoque.Produto;
import com.itextpdf.text.Element;

public class ProdutoPDFTRow extends PDFRow<Produto>
{
	private CustomPDFTCell codigoCell;
	private CustomPDFTCell nomeCell;
	private CustomPDFTCell quantidadeCell;
	private CustomPDFTCell preçoCell;
	private CustomPDFTCell preçoCompraCell;
	private CustomPDFTCell lucroCell;
	
	public ProdutoPDFTRow(Produto produto) 
	{
		codigoCell = new CustomPDFTCell(produto.getCodigo() + "");
		nomeCell = new CustomPDFTCell(produto.getNome());
		quantidadeCell = new CustomPDFTCell(produto.getQuantidade() + "");
		preçoCell = new CustomPDFTCell(String.format("%.2f", produto.getPreço()));
		preçoCompraCell = new CustomPDFTCell(String.format("%.2f", produto.getPreçoCompra()));
		lucroCell = new CustomPDFTCell(String.format("%.2f", produto.getPreço() - produto.getPreçoCompra()));
		
		codigoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nomeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		preçoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		preçoCompraCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		lucroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		nomeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		codigoCell.setVerticalAlignment(Element.ALIGN_CENTER);
		preçoCell.setVerticalAlignment(Element.ALIGN_CENTER);
		preçoCompraCell.setVerticalAlignment(Element.ALIGN_CENTER);
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

	public CustomPDFTCell getPreçoCell() {
		return preçoCell;
	}

	public CustomPDFTCell getPreçoCompraCell() {
		return preçoCompraCell;
	}

	public CustomPDFTCell getLucroCell() {
		return lucroCell;
	}
}
