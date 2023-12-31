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
		
		CustomPDFTCell codigoCell = new CustomPDFTCell("Cˇdigo");
		CustomPDFTCell nomeCell = new CustomPDFTCell("Nome");
		CustomPDFTCell quantidadeCell = new CustomPDFTCell("Estoque (un.)");
		CustomPDFTCell prešoCell = new CustomPDFTCell("Prešo (R$)");
		CustomPDFTCell prešoCompraCell = new CustomPDFTCell("Prešo de compra (R$)");
		CustomPDFTCell lucroCell = new CustomPDFTCell("Lucro (R$)");
		
		codigoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nomeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		prešoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		prešoCompraCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		lucroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		nomeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		codigoCell.setVerticalAlignment(Element.ALIGN_CENTER);
		prešoCell.setVerticalAlignment(Element.ALIGN_CENTER);
		prešoCompraCell.setVerticalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		lucroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		GrayColor grayColor = new GrayColor(0.85f);
		
		codigoCell.setBackgroundColor(grayColor);
		nomeCell.setBackgroundColor(grayColor);
		quantidadeCell.setBackgroundColor(grayColor);
		prešoCell.setBackgroundColor(grayColor);
		prešoCompraCell.setBackgroundColor(grayColor);
		lucroCell.setBackgroundColor(grayColor);
		
		addCell(codigoCell);
		addCell(nomeCell);
		addCell(quantidadeCell);
		addCell(prešoCell);
		addCell(prešoCompraCell);
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
		addCell(row.getPrešoCell());
		addCell(row.getPrešoCompraCell());
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
