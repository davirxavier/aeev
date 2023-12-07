package com.davixavier.utils.pdf;

import java.util.List;

import com.davixavier.entidades.estoque.Produto;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;

public class ProdutoPDFTable extends PDFTable<ProdutoPDFTRow, Produto>
{
	public ProdutoPDFTable() 
	{
		super(6);
		
		setTotalWidth(520);
		
		CustomPDFTCell codigoCell = new CustomPDFTCell("Código");
		CustomPDFTCell nomeCell = new CustomPDFTCell("Nome");
		CustomPDFTCell quantidadeCell = new CustomPDFTCell("Estoque (un.)");
		CustomPDFTCell preçoCell = new CustomPDFTCell("Preço (R$)");
		CustomPDFTCell preçoCompraCell = new CustomPDFTCell("Preço de compra (R$)");
		CustomPDFTCell lucroCell = new CustomPDFTCell("Lucro (R$)");
		
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
		lucroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		GrayColor grayColor = new GrayColor(0.85f);
		
		codigoCell.setBackgroundColor(grayColor);
		nomeCell.setBackgroundColor(grayColor);
		quantidadeCell.setBackgroundColor(grayColor);
		preçoCell.setBackgroundColor(grayColor);
		preçoCompraCell.setBackgroundColor(grayColor);
		lucroCell.setBackgroundColor(grayColor);
		
		addCell(codigoCell);
		addCell(nomeCell);
		addCell(quantidadeCell);
		addCell(preçoCell);
		addCell(preçoCompraCell);
		addCell(lucroCell);
		
		float[] widths = {0.5f, 4f, 1f, 1f, 1f, 1f};
		try 
		{
			setWidths(widths);
		} catch (DocumentException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addRow(ProdutoPDFTRow row)
	{
		addCell(row.getCodigoCell());
		addCell(row.getNomeCell());
		addCell(row.getQuantidadeCell());
		addCell(row.getPreçoCell());
		addCell(row.getPreçoCompraCell());
		addCell(row.getLucroCell());
	}

	@Override
	public void addRows(List<Produto> produtos) 
	{
		produtos.forEach(p ->
		{
			ProdutoPDFTRow row = new ProdutoPDFTRow(p);
			addRow(row);
		});
	}
}
